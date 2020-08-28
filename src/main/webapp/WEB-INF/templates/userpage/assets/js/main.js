/**
* Template Name: iPortfolio - v1.4.0
* Template URL: https://bootstrapmade.com/iportfolio-bootstrap-portfolio-websites-template/
* Author: BootstrapMade.com
* License: https://bootstrapmade.com/license/
*/
!(function($) {
  "use strict";

  // Hero typed
  if ($('.typed').length) {
    var typed_strings = $(".typed").data('typed-items');
    typed_strings = typed_strings.split(',')
    new Typed('.typed', {
      strings: typed_strings,
      loop: true,
      typeSpeed: 100,
      backSpeed: 50,
      backDelay: 2000
    });
  }

  // Smooth scroll for the navigation menu and links with .scrollto classes
  $(document).on('click', '.nav-menu a, .scrollto', function(e) {
    if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
      e.preventDefault();
      var target = $(this.hash);
      if (target.length) {

        var scrollto = target.offset().top;

        $('html, body').animate({
          scrollTop: scrollto
        }, 1500, 'easeInOutExpo');

        if ($(this).parents('.nav-menu, .mobile-nav').length) {
          $('.nav-menu .active, .mobile-nav .active').removeClass('active');
          $(this).closest('li').addClass('active');
        }

        if ($('body').hasClass('mobile-nav-active')) {
          $('body').removeClass('mobile-nav-active');
          $('.mobile-nav-toggle i').toggleClass('icofont-navigation-menu icofont-close');
        }
        return false;
      }
    }
  });

  // Activate smooth scroll on page load with hash links in the url
  $(document).ready(function() {
    if (window.location.hash) {
      var initial_nav = window.location.hash;
      if ($(initial_nav).length) {
        var scrollto = $(initial_nav).offset().top;
        $('html, body').animate({
          scrollTop: scrollto
        }, 1500, 'easeInOutExpo');
      }
    }
  });

  $(document).on('click', '.mobile-nav-toggle', function(e) {
    $('body').toggleClass('mobile-nav-active');
    $('.mobile-nav-toggle i').toggleClass('icofont-navigation-menu icofont-close');
  });

  $(document).click(function(e) {
    var container = $(".mobile-nav-toggle");
    if (!container.is(e.target) && container.has(e.target).length === 0) {
      if ($('body').hasClass('mobile-nav-active')) {
        $('body').removeClass('mobile-nav-active');
        $('.mobile-nav-toggle i').toggleClass('icofont-navigation-menu icofont-close');
      }
    }
  });

  // Navigation active state on scroll
  var nav_sections = $('section');
  var main_nav = $('.nav-menu, .mobile-nav');

  $(window).on('scroll', function() {
    var cur_pos = $(this).scrollTop() + 200;

    nav_sections.each(function() {
      var top = $(this).offset().top,
        bottom = top + $(this).outerHeight();

      if (cur_pos >= top && cur_pos <= bottom) {
        if (cur_pos <= bottom) {
          main_nav.find('li').removeClass('active');
        }
        main_nav.find('a[href="#' + $(this).attr('id') + '"]').parent('li').addClass('active');
      }
      if (cur_pos < 300) {
        $(".nav-menu ul:first li:first").addClass('active');
      }
    });
  });

  // Back to top button
  $(window).scroll(function() {
    if ($(this).scrollTop() > 100) {
      $('.back-to-top').fadeIn('slow');
    } else {
      $('.back-to-top').fadeOut('slow');
    }
  });

  $('.back-to-top').click(function() {
    $('html, body').animate({
      scrollTop: 0
    }, 1500, 'easeInOutExpo');
    return false;
  });

  // jQuery counterUp
  $('[data-toggle="counter-up"]').counterUp({
    delay: 10,
    time: 1000
  });

  // Skills section
  $('.skills-content').waypoint(function() {
    $('.progress .progress-bar').each(function() {
      $(this).css("width", $(this).attr("aria-valuenow") + '%');
    });
  }, {
    offset: '80%'
  });

  // Porfolio isotope and filter
  $(window).on('load', function() {
    var portfolioIsotope = $('.portfolio-container').isotope({
      itemSelector: '.portfolio-item',
      layoutMode: 'fitRows'
    });

    $('#portfolio-flters li').on('click', function() {
      $("#portfolio-flters li").removeClass('filter-active');
      $(this).addClass('filter-active');

      portfolioIsotope.isotope({
        filter: $(this).data('filter')
      });
      aos_init();
    });

    // Initiate venobox (lightbox feature used in portofilo)
    $(document).ready(function() {
      $('.venobox').venobox();
    });
  });

  // Testimonials carousel (uses the Owl Carousel library)
  $(".testimonials-carousel").owlCarousel({
    autoplay: true,
    dots: true,
    loop: true,
    responsive: {
      0: {
        items: 1
      },
      768: {
        items: 2
      },
      900: {
        items: 3
      }
    }
  });

  // Portfolio details carousel
  $(".portfolio-details-carousel").owlCarousel({
    autoplay: true,
    dots: true,
    loop: true,
    items: 1
  });

  // Init AOS
  function aos_init() {
    AOS.init({
      duration: 1000,
      easing: "ease-in-out-back",
      once: true
    });
  }
  $(window).on('load', function() {
    aos_init();
  });

})(jQuery);

//SETTINGS
function showName() {
  $("#cemail").css('display', 'none');
  $("#cpassword").css('display', 'none');
  $("#cname").css('display', 'block');
  $("#btnname").css('background-color', '#ff5757');
  $('#btnname').css('color', '#fff');


  $('#btnemail').css('color', '#ff5757');
  $("#btnemail").css('background-color', '#fff');
  $('#btnpassword').css('color', '#ff5757');
  $("#btnpassword").css('background-color', '#fff');
}
function showEmail() {
  $("#cemail").css('display', 'block');
  $("#cpassword").css('display', 'none');
  $("#cname").css('display', 'none');
  $("#btnemail").css('background-color', '#ff5757');
  $('#btnemail').css('color', '#fff');


  $('#btnname').css('color', '#ff5757');
  $("#btnname").css('background-color', '#fff');
  $('#btnpassword').css('color', '#ff5757');
  $("#btnpassword").css('background-color', '#fff');
}
function showPassword() {
  $("#cemail").css('display', 'none');
  $("#cpassword").css('display', 'block');
  $("#cname").css('display', 'none');
  $("#btnpassword").css('background-color', '#ff5757');
  $('#btnpassword').css('color', '#fff');


  $('#btnname').css('color', '#ff5757');
  $("#btnname").css('background-color', '#fff');
  $('#btnemail').css('color', '#ff5757');
  $("#btnemail").css('background-color', '#fff');
}
// ------ // --------


//-----------------------------------------------------------------

//JQUERY COM AJAX
// perform an ajax http get request
function editName(id) {

  var firstName = $('#name').val();
  var lastName = $('#hlastName').val();
  var email = $('#hemail').val();
  var password = $('#hpassword').val();
  var country = $('#hcountry').val();
  var phone = $('#hphone').val();

  $("#errormessage").css("display", "none");

  // perform an ajax http post request
  $.ajax({
    url: 'https://warpers.herokuapp.com/api/user/' + id,
    type: 'PUT',
    data: JSON.stringify({
      id: id,
      firstName: firstName,
      lastName: lastName,
      email: email,
      phone: phone,
      password: password,
      country: country
    }),
    async: true,
    contentType: 'application/json',
    success: successCallbackUpdate,
    error: errorCallbackUpdate
  });

  function successCallbackUpdate(data) {

    $("#remessage").css("display", "block");
    localStorage.setItem("firstName",firstName);
    setTimeout(
        function()
        {
          $("#remessage").css("display", "none");
        }, 3500);
    setTimeout( function () {
      location.reload();
    }, 4500);
  }

  function errorCallbackUpdate(request, status, error) {
    // do something with the error
    $("#errormessage").css("display", "block");
  }
}

// perform an ajax http get request
function editEmail(id) {

  var firstName = $('#hfirstName').val();
  var lastName = $('#hlastName').val();
  var email = $('#email').val();
  var password = $('#hpassword').val();
  var country = $('#hcountry').val();
  var phone = $('#hphone').val();

  $("#errormessage2").css("display", "none");

  // perform an ajax http post request
  $.ajax({
    url: 'https://warpers.herokuapp.com/api/user/' + id,
    type: 'PUT',
    data: JSON.stringify({
      id: id,
      firstName: firstName,
      lastName: lastName,
      email: email,
      phone: phone,
      password: password,
      country: country
    }),
    async: true,
    contentType: 'application/json',
    success: successCallbackUpdate2,
    error: errorCallbackUpdate2
  });

  function successCallbackUpdate2(data) {

    $("#remessage2").css("display", "block");
    localStorage.setItem("email",email);
    setTimeout(
        function()
        {
          $("#remessage2").css("display", "none");
        }, 3500);
    setTimeout( function () {
      location.reload();
    }, 4500);
  }

  function errorCallbackUpdate2(request, status, error) {
    // do something with the error
    $("#errormessage2").css("display", "block");
  }
}

// perform an ajax http get request
function editPassword(id) {

  var firstName = $('#hfirstName').val();
  var lastName = $('#hlastName').val();
  var email = $('#hemail').val();
  var password = $('#password').val();
  var cpassword = $('#confimar').val();
  var country = $('#hcountry').val();
  var phone = $('#hphone').val();

  $("#errormessage4").css("display", "none");
  $("#errormessage3").css("display", "none");

  if(password === cpassword) {
    // perform an ajax http post request
    $.ajax({
      url: 'https://warpers.herokuapp.com/api/user/' + id,
      type: 'PUT',
      data: JSON.stringify({
        id: id,
        firstName: firstName,
        lastName: lastName,
        email: email,
        phone: phone,
        password: password,
        country: country
      }),
      async: true,
      contentType: 'application/json',
      success: successCallbackUpdate3,
      error: errorCallbackUpdate3
    });

    function successCallbackUpdate3(data) {

      $("#remessage3").css("display", "block");
      localStorage.setItem("password",password);
      setTimeout(
          function()
          {
            $("#remessage3").css("display", "none");
          }, 3500);
      setTimeout( function () {
        location.reload();
      }, 4500);
    }

    function errorCallbackUpdate3(request, status, error) {
      // do something with the error
      $("#errormessage3").css("display", "block");
    }
  } else {
    $("#errormessage4").css("display", "block");
  }
}

function addNetflix() {
  $('#netflix').toggleClass('btn btn-subscribe btn btn-cancel');
  document.getElementById('netflix').innerHTML= 'Unsubscribe';
  localStorage.setItem('netflix', 'true');
}

function addHulu() {
  $('#hulu').toggleClass('btn btn-subscribe btn btn-cancel');
  document.getElementById('hulu').innerHTML= 'Unsubscribe';
  localStorage.setItem('hulu', 'true');
}

function addSpotify() {
  $('#spotify').toggleClass('btn btn-subscribe btn btn-cancel');
  document.getElementById('spotify').innerHTML= 'Unsubscribe';
  localStorage.setItem('spotify', 'true');
}

function addHbo() {
  $('#hbo').toggleClass('btn btn-subscribe btn btn-cancel');
  document.getElementById('hbo').innerHTML= 'Unsubscribe';
  localStorage.setItem('hbo', 'true');
}

function addAmazon() {
  $('#amazon').toggleClass('btn btn-subscribe btn btn-cancel');
  document.getElementById('amazon').innerHTML= 'Unsubscribe';
  localStorage.setItem('amazon', 'true');
}

function addAppletv() {
  $('#appletv').toggleClass('btn btn-subscribe btn btn-cancel');
  document.getElementById('appletv').innerHTML= 'Unsubscribe';
  localStorage.setItem('appletv', 'true');
}

function addDisney() {
  $('#disney').toggleClass('btn btn-subscribe btn btn-cancel');
  document.getElementById('disney').innerHTML= 'Unsubscribe';
  localStorage.setItem('disney', 'true');
}

function addApplem() {
  $('#applem').toggleClass('btn btn-subscribe btn btn-cancel');
  document.getElementById('applem').innerHTML= 'Unsubscribe';
  localStorage.setItem('applem', 'true');
}

//-----------------------------------------------------------------