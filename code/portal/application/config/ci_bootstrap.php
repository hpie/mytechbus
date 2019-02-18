<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/*
| -------------------------------------------------------------------------
| CI Bootstrap 3 Configuration
| -------------------------------------------------------------------------
| This file lets you define default values to be passed into views 
| when calling MY_Controller's render() function. 
| 
| See example and detailed explanation from:
| 	/application/config/ci_bootstrap_example.php
*/

$config['ci_bootstrap'] = array(

	// Site name
	'site_name' => 'My Tech Bus',
    // Default page title prefix
    'page_title_prefix' => 'MyTechBus - ',

	// Default page title
	'page_title' => '',

	// Default meta data
	'meta_data'	=> array(
		 'author' => 'HPIE',
        'description' => 'My Tech Bus',
        'keywords' => 'PHP,CodeIgniter,CRUD'
	),

	// Default scripts to embed at page head or end
	'scripts' => array(
		'head'	=> array(
            'assets/dist/frontend/jquery-2.2.4.min.js',
            'assets/dist/frontend/bootstrap.min.js',
		),
		'foot'	=> array(
			//'assets/dist/frontend/lib.min.js',
			//'assets/dist/frontend/app.min.js'
		),
	),

	// Default stylesheets to embed at page head
	'stylesheets' => array(
		'screen' => array(
			'assets/dist/frontend/bootstrap.min.css',
			'assets/dist/frontend/form-style.css',
			//'assets/dist/frontend/lib.min.css',
			'assets/dist/frontend/app.min.css'
		)
	),

	// Default CSS class for <body> tag
	'body_class' => '',
	
	// Multilingual settings

	'languages' => '',
	/*
	'languages' => array(
		'default'		=> 'en',
		'autoload'		=> array('general'),
		'available'		=> array(
			'en' => array(
				'label'	=> 'English',
				'value'	=> 'english'
			),
			'zh' => array(
				'label'	=> '繁體中文',
				'value'	=> 'traditional-chinese'
			),
			'cn' => array(
				'label'	=> '简体中文',
				'value'	=> 'simplified-chinese'
			),
			'es' => array(
				'label'	=> 'Español',
				'value' => 'spanish'
			)
		)
	),
	*/
	// Google Analytics User ID
	'ga_id' => '',

	// Menu items
	'menu' => array(
		'account' => array(
            'name' => 'My Account',
            'url' => 'account/',
            'icon' => 'fa fa-home',
        ),
        'auth' => array(
            'name' => 'Login / Logout',
            'url' => 'auth/login',
            'icon' => 'fa fa-users',
        ),
        'admin' => array(
            'name' => 'Admin',
            'url' => 'operator/',
            'icon' => 'fa fa-home',
        ),
	),

	// Login page
	'login_url' => '',

	// Restricted pages
	'page_auth' => array(
		'account' => array('members'),
	),

	// Email config
	'email' => array(
		'from_email'		=> '',
		'from_name'			=> '',
		'subject_prefix'	=> '',
		
		// Mailgun HTTP API
		'mailgun_api'		=> array(
			'domain'			=> '',
			'private_api_key'	=> ''
		),
	),

	// Debug tools
	'debug' => array(
		'view_data'	=> FALSE,
		'profiler'	=> FALSE
	),
);

/*
| -------------------------------------------------------------------------
| Override values from /application/config/config.php
| -------------------------------------------------------------------------
*/
$config['sess_cookie_name'] = 'ci_session_frontend';