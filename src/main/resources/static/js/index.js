document.addEventListener('DOMContentLoaded', function() {
    const navToggle = document.createElement('button');
    navToggle.className = 'nav__toggle';
    navToggle.innerHTML = `
        <span></span>
        <span></span>
        <span></span>
    `;
    
    const nav = document.querySelector('.nav');
    const navMenu = document.querySelector('.nav__menu');
    
    nav.insertBefore(navToggle, navMenu);
    
    navToggle.addEventListener('click', function() {
        this.classList.toggle('active');
        navMenu.classList.toggle('active');
    });
    
    navMenu.querySelectorAll('a').forEach(link => {
        link.addEventListener('click', () => {
            navToggle.classList.remove('active');
            navMenu.classList.remove('active');
        });
    });
    
    window.addEventListener('scroll', function() {
        const header = document.querySelector('.header');
        if (window.scrollY > 100) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }
    });

    // Elementos flotantes para hero
    const floatingElements = document.createElement('div');
    floatingElements.className = 'floating-elements';
    floatingElements.innerHTML = `
        <div class="floating-element"></div>
        <div class="floating-element"></div>
        <div class="floating-element"></div>
    `;
    document.querySelector('.hero').appendChild(floatingElements);

    function checkHeroScroll() {
        const heroContent = document.querySelector('.hero__content');
        const heroImage = document.querySelector('.hero__image');
        if (!heroContent || !heroImage) return;
        
        const heroPosition = heroContent.getBoundingClientRect().top;
        const screenPosition = window.innerHeight / 1.3;

        if (heroPosition < screenPosition) {
            heroContent.classList.add('reveal');
            heroImage.classList.add('reveal');
        }
    }

    // Animaciones de scroll para servicios
    function checkServicesScroll() {
        const serviceCards = document.querySelectorAll('.service__card');
        const servicesSection = document.querySelector('.services');
        
        if (!servicesSection || serviceCards.length === 0) return;
        
        const sectionPosition = servicesSection.getBoundingClientRect().top;
        const screenPosition = window.innerHeight / 1.2;

        if (sectionPosition < screenPosition) {
            serviceCards.forEach((card, index) => {
                setTimeout(() => {
                    card.classList.add('reveal');
                }, index * 150);
            });
        }
    }

    // Animaciones de scroll para proceso
    function checkProcessScroll() {
        const processSteps = document.querySelectorAll('.process__step');
        const processSection = document.querySelector('.process');
        
        if (!processSection || processSteps.length === 0) return;
        
        const sectionPosition = processSection.getBoundingClientRect().top;
        const screenPosition = window.innerHeight / 1.2;

        if (sectionPosition < screenPosition) {
            processSteps.forEach((step, index) => {
                setTimeout(() => {
                    step.classList.add('reveal');
                }, index * 200);
            });
        }
    }

    // Efectos hover interactivos para tarjetas de servicio
    function initServiceCardInteractions() {
        const serviceCards = document.querySelectorAll('.service__card');
        
        serviceCards.forEach(card => {
            card.addEventListener('mouseenter', function() {
                this.style.transform = 'translateY(-8px) scale(1.02)';
            });
            
            card.addEventListener('mouseleave', function() {
                this.style.transform = 'translateY(0) scale(1)';
            });
        });
    }

    // Efectos interactivos para pasos del proceso
    function initProcessInteractions() {
        const processSteps = document.querySelectorAll('.process__step');
        
        processSteps.forEach(step => {
            step.addEventListener('mouseenter', function() {
                const number = this.querySelector('.process__number');
                const image = this.querySelector('.process__image');
                
                if (number) {
                    number.style.transform = 'scale(1.15) rotate(8deg)';
                }
                if (image) {
                    image.style.transform = 'scale(1.08)';
                }
            });
            
            step.addEventListener('mouseleave', function() {
                const number = this.querySelector('.process__number');
                const image = this.querySelector('.process__image');
                
                if (number) {
                    number.style.transform = 'scale(1) rotate(0deg)';
                }
                if (image) {
                    image.style.transform = 'scale(1)';
                }
            });
        });
    }

    function initServicesCounter() {
        const serviceCards = document.querySelectorAll('.service__card');
        const counterElement = document.createElement('div');
        counterElement.className = 'services-counter';
        counterElement.innerHTML = `
            <div class="counter-badge">
                <i class="fas fa-procedures"></i>
                <span>${serviceCards.length} servicios disponibles</span>
            </div>
        `;
        
        // Insertar antes del grid de servicios
        const servicesGrid = document.querySelector('.services__grid');
        if (servicesGrid) {
            servicesGrid.parentNode.insertBefore(counterElement, servicesGrid);
        }
    }

    function initFooterWave() {
        const footer = document.querySelector('.footer');
        const waveSvg = `
            <div class="footer__wave">
                <svg data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 120" preserveAspectRatio="none">
                    <path d="M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z" class="shape-fill"></path>
                </svg>
            </div>
        `;
        
        if (footer) {
            footer.insertAdjacentHTML('afterbegin', waveSvg);
        }
    }

    // Smooth scroll para enlaces internos
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Inicializar todas las funcionalidades
    initServiceCardInteractions();
    initProcessInteractions();
    initServicesCounter();
    initFooterWave();

    window.addEventListener('scroll', function() {
        checkHeroScroll();
        checkServicesScroll();
        checkProcessScroll();
    });
    
    window.addEventListener('load', function() {
        checkHeroScroll();
        checkServicesScroll();
        checkProcessScroll();
    });

    checkHeroScroll();
    checkServicesScroll();
    checkProcessScroll();
});