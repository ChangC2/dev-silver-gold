<?php

use CodeIgniter\Router\RouteCollection;

/**
 * @var RouteCollection $routes
 */

$routes->get('/', 'site\Home');

//user site
$routes->get('page/user/login', 'site\User::login');
$routes->get('page/user/resetpw', 'site\User::resetpw');
$routes->get('register.html', 'site\User::register');
$routes->get('page/user/register', 'site\User::register');
$routes->get('page/user/verify', 'site\User::verify');
$routes->get('page/user/logout', 'site\User::logout');

$routes->get('page/user/account/profile', 'site\User::profile');
$routes->get('page/user/account/address', 'site\User::address');
$routes->get('page/user/account/bio', 'site\User::bio');
$routes->get('page/user/account/properties', 'site\User::properties');
$routes->get('page/user/account/comments', 'site\User::comments');


$routes->get('page/sellers', 'site\User::sellers');
$routes->get('page/profile', 'site\User::seller_profile');

//user api
$routes->post('api/user/login', 'api\User::login');
$routes->post('api/user/register', 'api\User::register');
$routes->post('api/user/resetpw', 'api\User::resetpw');
$routes->post('api/user/verify', 'api\User::verify');
$routes->post('api/user/resend_code', 'api\User::resend_code');
$routes->post('api/user/update_profile', 'api\User::update_profile');
$routes->post('api/user/update_address', 'api\User::update_address');
$routes->post('api/user/update_bio', 'api\User::update_bio');
$routes->post('api/user/delete', 'api\User::delete');

$routes->post('api/user/payment_init', 'api\User::payment_init');
$routes->post('api/user/cancel_subscription', 'api\User::cancel_subscription');
//property site
$routes->get('page/properties', 'site\Property::properties');
$routes->get('page/property', 'site\Property::property');

//comment site
$routes->get('page/comments', 'site\Comment::comments');
$routes->get('page/property', 'site\Property::property');

//property api
$routes->post('api/property/upload', 'api\Property::upload');
$routes->post('api/property/add', 'api\Property::add');
$routes->post('api/property/update', 'api\Property::update');
$routes->post('api/property/delete', 'api\Property::delete');
$routes->post('api/property/get_qrcode', 'api\Property::get_qrcode');
$routes->post('api/property/get_html', 'api\Property::get_html');


//comment api
$routes->post('api/comment/get', 'api\Comment::get');
$routes->get('api/comment/get_count', 'api\Comment::get_count');
$routes->post('api/comment/add', 'api\Comment::add');