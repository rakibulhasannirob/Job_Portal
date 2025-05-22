// Activate Bootstrap tooltips and popovers
document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl, {
            delay: { show: 300, hide: 100 }
        });
    });
    
    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl)
    });

    // Auto-dismiss alerts with fade effect
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert:not(.persistent)');
        alerts.forEach(function(alert) {
            if (alert) {
                alert.classList.add('fade-out');
                setTimeout(function() {
                    if (alert.parentNode) {
                        alert.parentNode.removeChild(alert);
                    }
                }, 500);
            }
        });
    }, 5000);

    // Dynamic text area that grows with content
    var textAreas = document.querySelectorAll('textarea.auto-grow');
    textAreas.forEach(function(textarea) {
        textarea.addEventListener('input', function() {
            this.style.height = 'auto';
            this.style.height = (this.scrollHeight) + 'px';
        });
        // Initialize height
        textarea.dispatchEvent(new Event('input'));
    });

    // Confirm delete actions with custom modal
    var deleteButtons = document.querySelectorAll('.confirm-delete');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            
            const itemName = this.getAttribute('data-item-name') || 'this item';
            const deleteUrl = this.getAttribute('href') || this.getAttribute('data-url');
            
            // Create modal programmatically
            const modalHTML = `
                <div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header bg-danger text-white">
                                <h5 class="modal-title">Confirm Deletion</h5>
                                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <p>Are you sure you want to delete ${itemName}? This action cannot be undone.</p>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <a href="${deleteUrl}" class="btn btn-danger">Delete</a>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            
            // Append modal to body
            const modalContainer = document.createElement('div');
            modalContainer.innerHTML = modalHTML;
            document.body.appendChild(modalContainer);
            
            // Show modal
            const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
            modal.show();
            
            // Remove modal from DOM after it's hidden
            document.getElementById('deleteConfirmModal').addEventListener('hidden.bs.modal', function() {
                document.body.removeChild(modalContainer);
            });
        });
    });
    
    // Add animation to job cards when they come into view
    const observeJobCards = () => {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('animate-in');
                    observer.unobserve(entry.target);
                }
            });
        }, {
            threshold: 0.1
        });
        
        document.querySelectorAll('.job-card').forEach(card => {
            observer.observe(card);
        });
    };
    
    if ('IntersectionObserver' in window) {
        observeJobCards();
    } else {
        // Fallback for browsers that don't support IntersectionObserver
        document.querySelectorAll('.job-card').forEach(card => {
            card.classList.add('animate-in');
        });
    }
    
    // Filter job listings with smooth transitions
    const jobFilter = document.getElementById('job-filter');
    if (jobFilter) {
        jobFilter.addEventListener('change', function() {
            const category = this.value;
            const jobCards = document.querySelectorAll('.job-card');
            
            jobCards.forEach(card => {
                card.classList.add('filtering');
                setTimeout(() => {
                    if (category === 'all' || card.getAttribute('data-category') === category) {
                        card.classList.remove('hidden');
                    } else {
                        card.classList.add('hidden');
                    }
                    setTimeout(() => {
                        card.classList.remove('filtering');
                    }, 50);
                }, 300);
            });
        });
    }
});