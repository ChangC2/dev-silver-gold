<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/*
| -------------------------------------------------------------------------
| URI ROUTING
| -------------------------------------------------------------------------
| This file lets you re-map URI requests to specific controller functions.
|
| Typically there is a one-to-one relationship between a URL string
| and its corresponding controller class/method. The segments in a
| URL normally follow this pattern:
|
|	example.com/class/method/id/
|
| In some instances, however, you may want to remap this relationship
| so that a different class/function is called than the one
| corresponding to the URL.
|
| Please see the user guide for complete details:
|
|	https://codeigniter.com/user_guide/general/routing.html
|
| -------------------------------------------------------------------------
| RESERVED ROUTES
| -------------------------------------------------------------------------
|
| There are three reserved routes:
|
|	$route['default_controller'] = 'welcome';
|
| This route indicates which controller class should be loaded if the
| URI contains no data. In the above example, the "welcome" class
| would be loaded.
|
|	$route['404_override'] = 'errors/page_missing';
|
| This route will tell the Router which controller/method to use if those
| provided in the URL cannot be matched to a valid route.
|
|	$route['translate_uri_dashes'] = FALSE;
|
| This is not exactly a route, but allows you to automatically route
| controller and method names that contain dashes. '-' isn't a valid
| class or method name character, so it requires translation.
| When you set this option to TRUE, it will replace ALL dashes in the
| controller and method URI segments.
|
| Examples:	my-controller/index	-> my_controller/index
|		my-controller/my-method	-> my_controller/my_method
*/

$route['default_controller'] = 'admin_site/login';
$route['login'] = 'admin_site/login';
$route['login_auth'] = 'admin_site/login_auth';
$route['plant_page/(:any)'] = 'admin_site/plant_page/$1';
$route['plant_page/(:any)/(:any)'] = 'admin_site/plant_page/$1/$2';
$route['plant_page'] = 'admin_site/plant_page';
$route['machine_page/(:any)'] = 'admin_site/machine_page/$1';
$route['machine_page'] = 'admin_site/machine_page';
$route['signout'] = 'admin_site/signout';
$route['machineDetail/(:any)'] = 'admin_site/machineDetail/$1';
$route['ganttDetail/(:any)/(:any)'] = 'admin_site/ganttDetail/$1/$2';

$route['ganttDetail2/(:any)/(:any)/(:any)'] = 'admin_site/ganttDetail2/$1/$2/$3';   // Mobile
$route['ganttDetail3'] = 'admin_site/ganttDetail3';   // Mobile


$route['emailPage/(:any)'] = 'admin_site/emailPage/$1';


$route['api/get_content'] = 'api/get_content';
$route['api/get_machine_detail'] = 'api/get_machine_detail';
$route['api/get_machine_time'] = 'api/get_machine_time';
$route['api/get_machine_detail_data'] = 'api/get_machine_detail_data';
$route['api/get_machine_detail_data_today'] = 'api/get_machine_detail_data_today';
$route['api/get_customer_detail'] = 'api/get_customer_detail';
$route['api/get_ganttData'] = 'api/get_ganttData';
$route['api/get_ganttData2'] = 'api/get_ganttData2';    // Mobile
$route['api/login'] = 'api/login';    // Mobile
$route['api/get_table'] = 'api/get_table';    // Mobile
$route['api/get_timezone'] = 'api/get_timezone';
$route['api/get_timezone2'] = 'api/get_timezone2';  // Mobile
$route['api/loginWithUserId'] = 'api/loginWithUserId';   // Mobile
$route['api/loginWithCustomerId'] = 'api/loginWithCustomerId';   // Mobile
$route['api/getGanttDataMobile'] = 'api/getGanttDataMobile';   // Mobile
$route['api/checkValidStatus'] = 'api/checkValidStatus';   // Mobile
$route['api/getJobData'] = 'api/getJobData';   // Mobile
$route['api/assignMachineToUser'] = 'api/assignMachineToUser';   // Mobile
$route['api/get_allHstData'] = 'api/get_allHstData';
$route['api/setEmailSetting'] = 'api/setEmailSetting';
$route['api/setMachineStatus'] = 'api/setMachineStatus';    // Mobile
$route['api/getAllMaintenanceInfo'] = 'api/getAllMaintenanceInfo';    // Mobile
$route['api/enterInMaintenance'] = 'api/enterInMaintenance';    // Mobile
$route['api/completeMaintenanceTask'] = 'api/completeMaintenanceTask';    // Mobile
$route['api/getAppSetting'] = 'api/getAppSetting';    // Mobile
$route['api/updateAppSetting'] = 'api/updateAppSetting';    // Mobile

$route['api/getTankTemperature'] = 'api/getTankTemperature';    // Mobile
$route['api/postTankTime'] = 'api/postTankTime';    // Mobile

$route['email/get_content'] = 'email/get_content';
$route['email/get_machine_detail'] = 'email/get_machine_detail';
$route['email/get_machine_time'] = 'email/get_machine_time';
$route['email/get_machine_detail_data'] = 'email/get_machine_detail_data';
$route['email/get_machine_detail_data_today'] = 'email/get_machine_detail_data_today';
$route['email/get_customer_detail'] = 'email/get_customer_detail';
$route['email/get_ganttData'] = 'email/get_ganttData';
$route['email/get_timezone'] = 'email/get_timezone';
$route['email/get_allHstData'] = 'email/get_allHstData';


$route['404_override'] = '';
$route['translate_uri_dashes'] = FALSE;
