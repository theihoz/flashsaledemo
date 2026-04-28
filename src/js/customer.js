import { store } from './store.js';

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
};

let countdownInterval;

// ===================== US7: Phân loại sản phẩm theo danh mục =====================

let currentCategory = 'all';

const renderCategoryFilter = () => {
    const container = document.getElementById('category-filter');
    if (!container) return;
    const categories = store.getCategories();
    
    const allBtn = `<button class="btn ${currentCategory === 'all' ? 'btn-primary' : ''}" 
        onclick="filterCategory('all')" style="padding:8px 16px; font-size:13px; border-radius:20px; cursor:pointer; ${currentCategory !== 'all' ? 'background:var(--card-bg); color:var(--text-primary); border:1px solid var(--card-border);' : ''}">
        Tất cả
    </button>`;
    
    const catBtns = categories.map(cat => 
        `<button class="btn ${currentCategory === cat ? 'btn-primary' : ''}" 
            onclick="filterCategory('${cat}')" style="padding:8px 16px; font-size:13px; border-radius:20px; cursor:pointer; ${currentCategory !== cat ? 'background:var(--card-bg); color:var(--text-primary); border:1px solid var(--card-border);' : ''}">
            ${cat}
        </button>`
    ).join('');
    
    container.innerHTML = allBtn + catBtns;
};

window.filterCategory = (category) => {
    currentCategory = category;
    renderCategoryFilter();
    
    const filtered = store.getProductsByCategory(category);
    const emptyMsg = document.getElementById('empty-category-msg');
    const grid = document.getElementById('product-grid');
    
    if (filtered.length === 0 && category !== 'all') {
        grid.style.display = 'none';
        emptyMsg.style.display = 'block';
        
        // Render top selling fallback
        const topSelling = store.getTopSelling();
        const topGrid = document.getElementById('top-selling-grid');
        topGrid.innerHTML = topSelling.map(p => `
            <div class="product-card glass" style="text-align:center; padding:15px;">
                <img src="${p.image}" loading="lazy" alt="${p.name}" class="product-image" style="height:120px;">
                <h4>${p.name}</h4>
                <p style="color:var(--accent-color); font-weight:bold;">${formatCurrency(p.originalPrice)}</p>
            </div>
        `).join('');
    } else {
        grid.style.display = '';
        emptyMsg.style.display = 'none';
        renderProducts(filtered);
    }
};

// ===================== Render Sản phẩm (hỗ trợ US1 + US7) =====================

const renderProducts = (filteredProducts) => {
    const grid = document.getElementById('product-grid');
    if (!grid) return;
    const products = filteredProducts || store.getProductsByCategory(currentCategory);
    const isFlashSale = store.isCampaignActive();

    grid.innerHTML = products.map(product => {
        const isSoldOutInfo = isFlashSale ? product.flashSaleInventory <= 0 : product.inventory <= 0;
        const inventoryText = isFlashSale 
            ? `Còn lại: <strong>${product.flashSaleInventory}</strong> suất` 
            : `Còn lại: <strong>${product.inventory}</strong> sản phẩm`;
        
        let priceHTML = `<span class="sale-price">${formatCurrency(product.originalPrice)}</span>`;
        
        let percentSold = 0;
        if (isFlashSale) {
            const salePrice = store.calculateFlashSalePrice(product.originalPrice);
            const saved = product.originalPrice - salePrice;
            priceHTML = `
                <span class="original-price">${formatCurrency(product.originalPrice)}</span>
                <span class="sale-price">${formatCurrency(salePrice)}</span>
                <div class="saved-badge">Tiết kiệm ${formatCurrency(saved)}</div>
            `;
            const total = product.flashSaleInventory + 5;
            percentSold = ((total - product.flashSaleInventory) / total) * 100;
            if (product.flashSaleInventory > 0) {
                percentSold = Math.max(10, percentSold);
            } else {
                percentSold = 100;
            }
        }

        return `
          <div class="product-card glass">
            <img src="${product.image}" loading="lazy" alt="${product.name}" class="product-image">
            <div class="product-info">
              <h3 class="product-title">${product.name}</h3>
              ${product.category ? `<span style="font-size:11px; background:var(--accent-color); color:white; padding:2px 8px; border-radius:10px; display:inline-block; margin-bottom:6px;">${product.category}</span>` : ''}
              <div class="price-container">
                ${priceHTML}
              </div>
              <div class="inventory">
                ${inventoryText}
                ${isFlashSale ? `
                    <div class="inventory-progress">
                       <div class="inventory-bar" style="width: ${percentSold}%"></div>
                    </div>
                ` : ''}
              </div>
              <div class="buy-action">
                <input type="number" id="qty-${product.id}" class="qty-input" value="1" min="1" ${isSoldOutInfo ? 'disabled' : ''}>
                <button 
                   class="btn btn-primary buy-btn" 
                   onclick="buyProduct(${product.id})"
                   ${isSoldOutInfo ? 'disabled' : ''}
                >
                  ${isSoldOutInfo ? 'Hết hàng' : 'Mua Ngay'}
                </button>
              </div>
            </div>
          </div>
        `;
    }).join('');
};

// ===================== Countdown (US1) =====================

const updateCountdown = () => {
    const campaign = store.getCampaign();
    const container = document.getElementById('countdown-container');
    
    if (!store.isCampaignActive()) {
        container.style.display = 'none';
        if (countdownInterval) clearInterval(countdownInterval);
        return;
    }

    container.style.display = 'block';
    
    const end = new Date(campaign.endTime).getTime();
    
    const tick = () => {
        const now = store.getCurrentTime().getTime();
        const distance = end - now;

        if (distance <= 0) {
            clearInterval(countdownInterval);
            renderProducts();
            container.style.display = 'none';
            return;
        }

        const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((distance % (1000 * 60)) / 1000);

        document.getElementById('hours').innerText = hours.toString().padStart(2, '0');
        document.getElementById('minutes').innerText = minutes.toString().padStart(2, '0');
        document.getElementById('seconds').innerText = seconds.toString().padStart(2, '0');
    };

    if (countdownInterval) clearInterval(countdownInterval);
    tick();
    countdownInterval = setInterval(tick, 1000);
};

// ===================== Mua hàng (US2) =====================

window.buyProduct = (productId) => {
    const qtyInput = document.getElementById(`qty-${productId}`);
    const quantity = qtyInput ? parseInt(qtyInput.value) : 1;
    
    if (quantity <= 0 || isNaN(quantity)) {
        const toast = document.getElementById('toast');
        toast.innerText = 'Số lượng mua phải lớn hơn 0';
        toast.className = 'toast show error';
        setTimeout(() => toast.className = 'toast', 3000);
        return;
    }

    const result = store.buyProduct(productId, quantity);
    const toast = document.getElementById('toast');
    toast.innerText = result.message;
    toast.className = `toast show ${result.success ? 'success' : 'error'}`;
    
    setTimeout(() => {
        toast.className = 'toast';
    }, 3000);

    if (result.success) {
        renderProducts();
        renderOrderHistory();
    }
};

// ===================== US8: Lịch sử đơn hàng chi tiết =====================

const renderOrderHistory = () => {
    const container = document.getElementById('order-history');
    if (!container) return;
    const orders = store.getOrderHistory();
    
    if (!orders || orders.length === 0) {
        container.innerHTML = `
            <div style="text-align:center; padding:40px 20px;">
                <p style="font-size:48px; margin-bottom:10px;">📦</p>
                <p style="font-size:18px; color:var(--text-secondary); margin-bottom:20px;">Bạn chưa có đơn hàng nào</p>
                <a href="#" onclick="window.scrollTo({top:0, behavior:'smooth'}); return false;" 
                   class="btn btn-primary" style="display:inline-block; padding:12px 24px; text-decoration:none;">
                    🔥 Khám phá deal Hot
                </a>
            </div>
        `;
        return;
    }
    
    let totalSavings = 0;
    const rows = orders.map((order, i) => {
        const savings = store.calculateSavings(order);
        totalSavings += savings;
        const date = new Date(order.timestamp).toLocaleString('vi-VN');
        return `
            <div class="combo-item glass" style="display:flex; justify-content:space-between; align-items:center; padding:15px; margin-bottom:8px; border-radius:8px;">
                <div>
                    <strong>${order.productName}</strong>
                    <div style="font-size:12px; color:var(--text-secondary);">${date} | SL: ${order.quantity}</div>
                </div>
                <div style="text-align:right;">
                    <div style="font-weight:bold; color:var(--accent-color);">${formatCurrency(order.total)}</div>
                    ${order.isFlashSale && savings > 0 ? `<div style="font-size:12px; color:var(--success);">Tiết kiệm ${formatCurrency(savings)}</div>` : ''}
                    <span style="font-size:11px; background:${order.isFlashSale ? '#fbbf24' : '#94a3b8'}; color:#1e293b; padding:2px 8px; border-radius:10px;">
                        ${order.isFlashSale ? '⚡ Flash Sale' : 'Giá thường'}
                    </span>
                </div>
            </div>
        `;
    }).join('');
    
    container.innerHTML = `
        ${totalSavings > 0 ? `<div class="glass" style="text-align:center; padding:15px; margin-bottom:15px; border-radius:8px; background:linear-gradient(135deg, #10b981 0%, #059669 100%); color:white;">
            💰 Tổng tiết kiệm: <strong>${formatCurrency(totalSavings)}</strong>
        </div>` : ''}
        ${rows}
    `;
};

// ===================== US5: Hiển thị Combo cho Khách hàng =====================

const renderCombos = () => {
    const section = document.getElementById('combo-section');
    const grid = document.getElementById('combo-grid');
    if (!section || !grid) return;

    const combos = store.getCombos();
    if (!combos || combos.length === 0) {
        section.style.display = 'none';
        return;
    }

    section.style.display = 'block';
    const isFlashSale = store.isCampaignActive();

    grid.innerHTML = combos.map(combo => {
        const details = store.getComboDetails(combo.id);
        if (!details || details.products.length === 0) return '';

        const productList = details.products.map(p => 
            `<div style="display:flex; align-items:center; gap:8px; margin-bottom:6px;">
                <img src="${p.image}" alt="${p.name}" style="width:36px; height:36px; border-radius:6px; object-fit:cover;">
                <span style="font-size:13px;">${p.name}</span>
            </div>`
        ).join('');

        const allInStock = details.products.every(p => 
            isFlashSale ? p.flashSaleInventory > 0 : p.inventory > 0
        );

        return `
          <div class="product-card glass" style="position:relative;">
            <div style="position:absolute; top:12px; right:12px; background:linear-gradient(135deg, #f59e0b, #ef4444); color:white; padding:4px 12px; border-radius:20px; font-size:12px; font-weight:bold;">
                COMBO -${combo.discountPercent}%
            </div>
            <div style="padding:20px; padding-top:45px;">
              <h3 style="font-size:18px; margin-bottom:12px;">🎁 ${combo.name}</h3>
              <div style="border:1px solid var(--card-border); border-radius:8px; padding:10px; margin-bottom:12px;">
                ${productList}
              </div>
              <div class="price-container" style="margin-bottom:8px;">
                <span class="original-price">${formatCurrency(details.originalTotal)}</span>
                <span class="sale-price" style="font-size:22px;">${formatCurrency(details.comboPrice)}</span>
              </div>
              <div class="saved-badge" style="margin-bottom:12px;">
                Tiết kiệm ${formatCurrency(details.savedAmount)}
              </div>
              <button 
                class="btn btn-primary buy-btn" 
                style="width:100%; padding:12px; font-size:15px; ${!allInStock ? 'opacity:0.5; cursor:not-allowed;' : ''}"
                onclick="buyCombo(${combo.id})"
                ${!allInStock ? 'disabled' : ''}
              >
                ${allInStock ? '🛒 Mua Combo' : 'Hết hàng'}
              </button>
            </div>
          </div>
        `;
    }).filter(Boolean).join('');
};

window.buyCombo = (comboId) => {
    const result = store.buyCombo(comboId);
    const toast = document.getElementById('toast');
    toast.innerText = result.message;
    toast.className = `toast show ${result.success ? 'success' : 'error'}`;
    setTimeout(() => { toast.className = 'toast'; }, 3000);

    if (result.success) {
        renderProducts();
        renderCombos();
        renderOrderHistory();
    }
};

// ===================== Events =====================

window.addEventListener('storeUpdated', () => {
    renderProducts();
    updateCountdown();
    renderCombos();
    renderOrderHistory();
});

// ===================== Init =====================
renderProducts();
updateCountdown();
renderCategoryFilter();
renderCombos();
renderOrderHistory();
