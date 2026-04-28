// src/js/store.js
class Store {
  constructor() {
    this.initData();
  }

  initData() {
    const defaultProducts = [
      { id: 1, name: 'Son MAC Ruby Woo', originalPrice: 650000, inventory: 100, flashSaleInventory: 15, category: 'Son môi', image: 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=500&q=80' },
      { id: 2, name: 'Nước hoa Chanel N°5', originalPrice: 3500000, inventory: 50, flashSaleInventory: 5, category: 'Nước hoa', image: 'https://images.unsplash.com/photo-1594035910387-fea47794261f?w=500&q=80' },
      { id: 3, name: 'Kem chống nắng La Roche-Posay', originalPrice: 480000, inventory: 200, flashSaleInventory: 30, category: 'Kem dưỡng', image: 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=500&q=80' }
    ];

    const currentProds = JSON.parse(localStorage.getItem('products'));
    if (!currentProds || currentProds[0].name.includes('Tai nghe') || !currentProds[0].category) {
        localStorage.setItem('products', JSON.stringify(defaultProducts));
        localStorage.removeItem('campaign');
        localStorage.removeItem('combos');
        localStorage.removeItem('orders');
        localStorage.removeItem('campaignProducts');
    }

    if (!localStorage.getItem('campaign')) {
      // Create a default active campaign ending in 1 hour for demo purposes
      const now = new Date();
      const end = new Date(now.getTime() + 60 * 60 * 1000);
      localStorage.setItem('campaign', JSON.stringify({
        isActive: true,
        startTime: now.toISOString(),
        endTime: end.toISOString(),
        discountPercent: 20 // 20% for cosmetics is common
      }));
    }
    if (!localStorage.getItem('combos')) localStorage.setItem('combos', JSON.stringify([]));
    if (!localStorage.getItem('orders')) localStorage.setItem('orders', JSON.stringify([]));
    if (!localStorage.getItem('campaignProducts')) localStorage.setItem('campaignProducts', JSON.stringify([]));
  }

  get(key) {
    return JSON.parse(localStorage.getItem(key));
  }

  set(key, data) {
    localStorage.setItem(key, JSON.stringify(data));
    window.dispatchEvent(new Event('storeUpdated'));
  }

  getProducts() {
    return this.get('products');
  }

  getCampaign() {
    return this.get('campaign');
  }

  getCurrentTime() {
    return new Date();
  }

  isCampaignActive() {
    const campaign = this.getCampaign();
    if (!campaign || !campaign.isActive) return false;
    const now = this.getCurrentTime();
    const start = new Date(campaign.startTime);
    const end = new Date(campaign.endTime);
    return now >= start && now <= end;
  }

  calculateFlashSalePrice(originalPrice) {
    const campaign = this.getCampaign();
    if (!this.isCampaignActive()) return originalPrice;
    return originalPrice * (1 - campaign.discountPercent / 100);
  }

  buyProduct(productId, quantity) {
    const products = this.getProducts();
    const productIndex = products.findIndex(p => p.id === productId);
    if (productIndex === -1) return { success: false, message: 'Không tìm thấy sản phẩm' };

    const product = products[productIndex];
    const isFlashSale = this.isCampaignActive();

    if (isFlashSale) {
        if (product.flashSaleInventory < quantity) {
            return { success: false, message: 'Sản phẩm đã hết suất Flash Sale' };
        }
        products[productIndex].flashSaleInventory -= quantity;
        products[productIndex].inventory -= quantity; // also reduce total inventory
    } else {
        if (product.inventory < quantity) {
             return { success: false, message: 'Hết hàng' };
        }
        products[productIndex].inventory -= quantity;
    }
    
    // Save order
    const price = isFlashSale ? this.calculateFlashSalePrice(product.originalPrice) : product.originalPrice;
    const orders = this.get('orders');
    orders.push({
      productId,
      productName: product.name,
      quantity,
      price,
      total: price * quantity,
      isFlashSale,
      timestamp: new Date().toISOString()
    });
    
    this.set('products', products);
    this.set('orders', orders);
    return { success: true, message: 'Đặt hàng thành công!' };
  }

  getAnalytics() {
    const orders = this.get('orders');
    const products = this.getProducts();
    
    // Total revenue from flash sale
    const flashSaleOrders = orders.filter(o => o.isFlashSale);
    const revenue = flashSaleOrders.reduce((sum, order) => sum + order.total, 0);
    
    // Calculate initial flash sale inventory by adding current + sold
    const totalCurrentFlashSaleInventory = products.reduce((sum, p) => sum + p.flashSaleInventory, 0);
    const totalSoldFlashSale = flashSaleOrders.reduce((sum, o) => sum + o.quantity, 0);
    const totalInitialInventory = totalCurrentFlashSaleInventory + totalSoldFlashSale;
    
    let soldPercentage = 0;
    if (totalInitialInventory > 0) {
       soldPercentage = (totalSoldFlashSale / totalInitialInventory) * 100;
    }

    return {
       revenue,
       soldPercentage: soldPercentage.toFixed(2),
       totalInitialInventory,
       totalSoldFlashSale
    };
  }

  saveCampaign(startTimeStr, endTimeStr, discountPct) {
    if (discountPct > 50) {
      return { success: false, message: 'Mức giảm không được vượt quá 50%' };
    }
    const startTime = new Date(startTimeStr);
    const endTime = new Date(endTimeStr);
    
    if (startTime >= endTime) {
      return { success: false, message: 'Thời gian bắt đầu phải trước thời gian kết thúc' };
    }
    
    this.set('campaign', {
      isActive: true,
      startTime: startTime.toISOString(),
      endTime: endTime.toISOString(),
      discountPercent: discountPct
    });
    return { success: true, message: 'Chiến dịch đã được lên lịch' };
  }

  createCombo(name, productIds, discountPercent) {
      const products = this.getProducts();
      // Check inventory
      for (const id of productIds) {
          const p = products.find(prod => prod.id === id);
          if (!p || p.inventory <= 0) {
             return { success: false, message: `Sản phẩm ${p ? p.name : id} không đủ tồn kho để tạo Combo` };
          }
      }
      
      const combos = this.get('combos');
      combos.push({
          id: Date.now(),
          name,
          productIds,
          discountPercent
      });
      this.set('combos', combos);
      return { success: true, message: 'Tạo Combo thành công!' };
  }

  getCombos() {
      return this.get('combos');
  }

  getComboDetails(comboId) {
      const combos = this.getCombos();
      const combo = combos.find(c => c.id === comboId);
      if (!combo) return null;

      const products = this.getProducts();
      const comboProducts = combo.productIds
          .map(id => products.find(p => p.id === id))
          .filter(Boolean);

      const originalTotal = comboProducts.reduce((sum, p) => sum + p.originalPrice, 0);
      const isFlashSale = this.isCampaignActive();
      let saleTotal = originalTotal;
      if (isFlashSale) {
          saleTotal = comboProducts.reduce((sum, p) => sum + this.calculateFlashSalePrice(p.originalPrice), 0);
      }
      const comboPrice = saleTotal * (1 - combo.discountPercent / 100);

      return {
          ...combo,
          products: comboProducts,
          originalTotal,
          saleTotal,
          comboPrice,
          savedAmount: originalTotal - comboPrice
      };
  }

  buyCombo(comboId) {
      const combo = this.getCombos().find(c => c.id === comboId);
      if (!combo) return { success: false, message: 'Combo không tồn tại' };

      const products = this.getProducts();
      const isFlashSale = this.isCampaignActive();

      // Check tồn kho tất cả SP trong combo
      for (const pid of combo.productIds) {
          const p = products.find(pr => pr.id === pid);
          if (!p) return { success: false, message: `Sản phẩm ID ${pid} không tìm thấy` };
          if (isFlashSale && p.flashSaleInventory <= 0) {
              return { success: false, message: `${p.name} đã hết suất Flash Sale` };
          }
          if (!isFlashSale && p.inventory <= 0) {
              return { success: false, message: `${p.name} đã hết hàng` };
          }
      }

      // Trừ kho từng SP
      for (const pid of combo.productIds) {
          const idx = products.findIndex(pr => pr.id === pid);
          if (isFlashSale) {
              products[idx].flashSaleInventory -= 1;
          }
          products[idx].inventory -= 1;
      }

      // Tính giá combo
      const details = this.getComboDetails(comboId);
      const orders = this.get('orders');
      orders.push({
          productId: comboId,
          productName: `🎁 ${combo.name}`,
          quantity: 1,
          price: details.comboPrice,
          total: details.comboPrice,
          isFlashSale,
          isCombo: true,
          timestamp: new Date().toISOString()
      });

      this.set('products', products);
      this.set('orders', orders);
      return { success: true, message: `Mua ${combo.name} thành công!` };
  }

  // ===================== US6: Quản lý sản phẩm Flash Sale =====================

  addFlashSaleProduct(productId, flashSalePrice, limitQuantity) {
      const campaignProducts = this.get('campaignProducts');
      const exists = campaignProducts.find(p => p.productId === productId);
      if (exists) {
          return { success: false, message: 'Sản phẩm đã tồn tại trong chiến dịch Flash Sale này' };
      }
      if (flashSalePrice <= 0) {
          return { success: false, message: 'Giá Flash Sale phải lớn hơn 0' };
      }
      if (limitQuantity <= 0) {
          return { success: false, message: 'Số lượng giới hạn phải lớn hơn 0' };
      }
      campaignProducts.push({ productId, flashSalePrice, limitQuantity });
      this.set('campaignProducts', campaignProducts);
      return { success: true, message: 'Thêm sản phẩm thành công' };
  }

  removeFlashSaleProduct(productId) {
      let campaignProducts = this.get('campaignProducts');
      const index = campaignProducts.findIndex(p => p.productId === productId);
      if (index === -1) {
          return { success: false, message: 'Sản phẩm không tồn tại trong chiến dịch' };
      }
      campaignProducts.splice(index, 1);
      this.set('campaignProducts', campaignProducts);
      return { success: true, message: 'Đã xóa sản phẩm khỏi chiến dịch' };
  }

  getCampaignProducts() {
      return this.get('campaignProducts');
  }

  // ===================== US7: Phân loại sản phẩm theo danh mục =====================

  getCategories() {
      const products = this.getProducts();
      const categories = [...new Set(products.map(p => p.category).filter(Boolean))];
      return categories;
  }

  getProductsByCategory(category) {
      const products = this.getProducts();
      if (!category || category === 'all') return products;
      return products.filter(p => p.category === category);
  }

  getTopSelling() {
      const products = this.getProducts();
      return products.sort((a, b) => {
          const aSold = (a.flashSaleInventory !== undefined) ? (100 - a.flashSaleInventory) : 0;
          const bSold = (b.flashSaleInventory !== undefined) ? (100 - b.flashSaleInventory) : 0;
          return bSold - aSold;
      }).slice(0, 3);
  }

  // ===================== US8: Lịch sử đơn hàng chi tiết =====================

  getOrderHistory() {
      return this.get('orders');
  }

  calculateSavings(order) {
      if (!order.isFlashSale) return 0;
      const products = this.getProducts();
      const product = products.find(p => p.id === order.productId);
      if (!product) return 0;
      return (product.originalPrice - order.price) * order.quantity;
  }

  getTotalSavings() {
      const orders = this.getOrderHistory();
      return orders
          .filter(o => o.isFlashSale)
          .reduce((sum, o) => sum + this.calculateSavings(o), 0);
  }
}

export const store = new Store();
