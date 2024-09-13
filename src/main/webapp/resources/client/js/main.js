(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner(0);


    // Fixed Navbar
    $(window).scroll(function () {
        if ($(window).width() < 992) {
            if ($(this).scrollTop() > 55) {
                $('.fixed-top').addClass('shadow');
            } else {
                $('.fixed-top').removeClass('shadow');
            }
        } else {
            if ($(this).scrollTop() > 55) {
                $('.fixed-top').addClass('shadow').css('top', -55);
            } else {
                $('.fixed-top').removeClass('shadow').css('top', 0);
            }
        }
    });


    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({ scrollTop: 0 }, 1500, 'easeInOutExpo');
        return false;
    });


    // Testimonial carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 2000,
        center: false,
        dots: true,
        loop: true,
        margin: 25,
        nav: true,
        navText: [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsiveClass: true,
        responsive: {
            0: {
                items: 1
            },
            576: {
                items: 1
            },
            768: {
                items: 1
            },
            992: {
                items: 2
            },
            1200: {
                items: 2
            }
        }
    });


    // vegetable carousel
    $(".vegetable-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1500,
        center: false,
        dots: true,
        loop: true,
        margin: 25,
        nav: true,
        navText: [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsiveClass: true,
        responsive: {
            0: {
                items: 1
            },
            576: {
                items: 1
            },
            768: {
                items: 2
            },
            992: {
                items: 3
            },
            1200: {
                items: 4
            }
        }
    });


    // Modal Video
    $(document).ready(function () {
        var $videoSrc;
        $('.btn-play').click(function () {
            $videoSrc = $(this).data("src");
        });
        console.log($videoSrc);

        $('#videoModal').on('shown.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc + "?autoplay=1&amp;modestbranding=1&amp;showinfo=0");
        })

        $('#videoModal').on('hide.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc);
        })
        // add active class to header
        const navE = $('#navbarCollapse');
        const currentUrl = window.location.pathname;

        navE.find('a.nav-link').each(function () {
            const link = $(this);
            const href = link.attr('href');

            if (href === currentUrl) {
                link.addClass('active');
            } else {
                link.removeClass('active');
            }
        });
    });



    // Product Quantity
    $('.quantity button').on('click', function () {
        let change = 0;
        var button = $(this);
        var oldValue = button.parent().parent().find('input').val();
        if (button.hasClass('btn-plus')) {
            var newVal = parseFloat(oldValue) + 1;
            change = 1;
        } else {
            if (oldValue > 1) {
                var newVal = parseFloat(oldValue) - 1;
                change = -1;
            } else {
                newVal = 1;
            }
        }
        const input = button.parent().parent().find('input');
        input.val(newVal);

        // set form index
        const index = input.attr('data-cart-detail-index');
        const el = document.getElementById(`cartDetails${index}.quantity`);
        $(el).val(newVal);

        // get price
        const price = input.attr("data-cart-detail-price");
        const id = input.attr("data-cart-detail-id");

        const priceE = $(`p[data-cart-detail-id='${id}']`);
        if (priceE) {
            const newPrice = +price * newVal;
            priceE.text(formatCurrency(newPrice.toFixed(2)) + " đ");
        }

        // update total price
        const totalE = $(`p[data-cart-total-price]`);

        if (totalE && totalE.length) {
            const currentTotal = totalE.first().attr("data-cart-total-price");
            let newTotal = +currentTotal;
            if (change === 0) {
                newTotal = + currentTotal;
            }
            else {
                newTotal = change * (+price) + (+currentTotal);
            }

            change = 0;

            // update
            totalE?.each(function (index, e) {
                //update text 
                $(totalE[index]).text(formatCurrency(newTotal.toFixed(2)) + " đ");

                //update data att
                $(totalE[index]).attr("data-cart-total-price", newTotal);
            })
        }
    });

    // Format currency
    const priceE = $('p[data-cart-detail-price');
    priceE.each(function (index, e) {
        const price = + $(priceE[index]).attr("data-cart-detail-price");
        $(priceE[index]).text(formatCurrency(price.toFixed(2)) + " đ");
    });

    const totalPriceE = $('p[data-cart-total-price]');
    totalPriceE.each(function (index, e) {
        const price = + $(totalPriceE[index]).attr("data-cart-total-price");
        $(totalPriceE[index]).text(formatCurrency(price.toFixed(2)) + " đ");
    });

    const tPriceE = $('p[data-cart-detail-tPrice]');
    tPriceE.each(function (index, e) {
        const price = + $(tPriceE[index]).attr("data-cart-detail-tPrice");
        $(tPriceE[index]).text(formatCurrency(price.toFixed(2)) + " đ");
    });

    function formatCurrency(value) {
        const formatter = new Intl.NumberFormat('vi-VN', {
            style: 'decimal',
            minimumFractionDigits: 0,
        });

        let formatted = formatter.format(value);

        formatted = formatted.replace(/\./g, ',');
        return formatted;
    }

})(jQuery);

