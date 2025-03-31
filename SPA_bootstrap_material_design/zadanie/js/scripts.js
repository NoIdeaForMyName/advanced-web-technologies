// Inicjalizacja wykresu popularności
function initPopularityChart() {
    const ctx = document.getElementById('popularityChart');
    if (ctx) {
      new Chart(ctx, {
        type: 'bar',
        data: {
          labels: ['Barcelona', 'Tokio', 'Nowy Jork', 'Rio de Janeiro', 'Marrakesz', 'Sydney', 'Bora Bora'],
            datasets: [{
              label: 'Popularność kierunków w 2024 (%)',
              data: [85, 92, 88, 78, 70, 75, 82],
              backgroundColor: 'rgba(54, 162, 235, 0.7)',
              borderColor: 'rgba(54, 162, 235, 1)',
              borderWidth: 1
            }]
        },
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true,
              max: 100
            }
          }
        }
      });
    }
  }
  
  // Inicjalizacja AOS
  function initAOS() {
    AOS.init({
      duration: 1000,
      once: true
    });
  }
  
  // Funkcja do obsługi galerii
  function initGallery(modal) {
    const mainImage = modal.querySelector('.main-image');
    const thumbnails = modal.querySelectorAll('.thumbnail-img');
    const thumbnailWrapper = modal.querySelector('.thumbnail-wrapper');
    const scrollLeftBtn = modal.querySelector('.scroll-btn.left');
    const scrollRightBtn = modal.querySelector('.scroll-btn.right');

    if (!mainImage || thumbnails.length === 0 || !thumbnailWrapper) return;

    // Ustawienie pierwszego zdjęcia jako aktywnego
    function resetGallery() {
        if (thumbnails.length > 0) {
            const firstThumbnail = thumbnails[0];
            setTimeout(() => {
                mainImage.setAttribute('src', firstThumbnail.getAttribute('src'));
                mainImage.setAttribute('alt', firstThumbnail.getAttribute('alt'));
                mainImage.style.opacity = '1';

                thumbnails.forEach(img => img.classList.remove('active'));
                firstThumbnail.classList.add('active');

                // Przewinięcie miniatur na początek
                thumbnailWrapper.scrollLeft = 0;
                
                // Wymuszenie sprawdzenia przycisków po resecie
                setTimeout(checkScrollButtons, 100);
            }, 300);
        }
    }

    // Obsługa kliknięć w miniaturki
    thumbnails.forEach(thumb => {
        thumb.addEventListener('click', function () {
            const newSrc = this.getAttribute('src');
            const newAlt = this.getAttribute('alt');

            // Animacja zanikania i pojawiania
            mainImage.style.opacity = '0';
            setTimeout(() => {
                mainImage.setAttribute('src', newSrc);
                mainImage.setAttribute('alt', newAlt);
                mainImage.style.opacity = '1';

                thumbnails.forEach(img => img.classList.remove('active'));
                this.classList.add('active');

                // Przewinięcie miniaturki do widoku
                this.scrollIntoView({ behavior: "smooth", block: "nearest", inline: "center" });
            }, 100);
        });
    });

    // Przewijanie w lewo
    scrollLeftBtn?.addEventListener('click', () => {
        thumbnailWrapper.scrollBy({ left: -200, behavior: 'smooth' });
    });

    // Przewijanie w prawo
    scrollRightBtn?.addEventListener('click', () => {
        thumbnailWrapper.scrollBy({ left: 200, behavior: 'smooth' });
    });

    // Aktualizacja widoczności przycisków przewijania
    function checkScrollButtons() {
        if (!scrollLeftBtn || !scrollRightBtn) return;
        
        scrollLeftBtn.style.display = thumbnailWrapper.scrollLeft > 0 ? 'flex' : 'none';
        scrollRightBtn.style.display =
            Math.ceil(thumbnailWrapper.scrollLeft + thumbnailWrapper.clientWidth) < thumbnailWrapper.scrollWidth
                ? 'flex'
                : 'none';
    }

    thumbnailWrapper?.addEventListener('scroll', checkScrollButtons);
    window.addEventListener('resize', checkScrollButtons);

    // Resetowanie galerii na starcie
    resetGallery();

    // Obsługa modali - dla wszystkich modali na stronie
    modal.addEventListener('show.bs.modal', resetGallery);
  }
  
  // Inicjalizacja wszystkich skryptów po załadowaniu DOM
  document.addEventListener('DOMContentLoaded', function() {
    initAOS();
    initPopularityChart();
    document.querySelectorAll('.modal').forEach(initGallery);
  });