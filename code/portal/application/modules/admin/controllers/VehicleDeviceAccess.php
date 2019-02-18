<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class VehicleDeviceAccess extends Admin_Controller {

	public function __construct()
	{
		parent::__construct();
		$this->load->library('form_builder');
	}

	// Frontend User CRUD
	public function index()
	{
		$loggedinUser = $this->mUser;
		$crud = $this->generate_crud('vehicle_device_access');
		$crud->columns('operator_id', 'loginid', 'password', 'device_imie', 'route_id', 'vehicle_id', 'device_last_login', 'latitude', 'longitude', 'altitude', 'altutude_accuracy', 'device_login_attempts', 'access_status');
		
		
		//Show for add
		$crud->add_fields('operator_id', 'loginid', 'password', 'device_imie', 'route_id', 'vehicle_id', 'access_status');
		
		//Show only for Update
		$crud->edit_fields('operator_id','loginid', 'password', 'device_imie', 'route_id', 'vehicle_id', 'access_status');
			
		$crud->field_type('access_status','dropdown',array('ACTIVE'=>'ACTIVE','INACTIVE'=>'INACTIVE','DELETED'=>'DELETED'));	

		
		//how to add others? and create a new record if others
		$crud->set_relation('operator_id','vehicle_operators','{operator_name}-{operator_city}',array('operator_status' => 'ACTIVE'), 'operator_name ASC');
		
		//how to add others? and create a new record if others
		$crud->set_relation('device_imie','vehicle_operator_devices','{device_imie}-{device_number}',array('device_status' => 'ACTIVE'), 'device_number ASC');
		
		//how to add others? and create a new record if others
		$crud->set_relation('route_id','master_routes','{route_start_stage}-{route_end_stage}',array('route_status' => 'ACTIVE'), 'route_code, route_start_stage ASC');
		
		//how to add others? and create a new record if others
		$crud->set_relation('vehicle_id','vehicle_master','{vehicle_number}-{vehicle_type}',array('vehicle_status' => 'ACTIVE'), 'operator_id, vehicle_number ASC');
		
		/*
		// only webmaster and admin can change member groups
		if ($crud->getState()=='list' || $this->ion_auth->in_group(array('webmaster', 'admin')))
		{
			$crud->set_relation_n_n('groups', 'users_groups', 'groups', 'user_id', 'group_id', 'name');
		}

		// only webmaster and admin can reset user password
		if ($this->ion_auth->in_group(array('webmaster', 'admin')))
		{
			$crud->add_action('Reset Password', '', 'admin/user/reset_password', 'fa fa-repeat');
		}
		*/
		
		$state = $crud->getState();
		$state_info = $crud->getStateInfo();
		// add edit logic
		if ($state == 'add' || $state == 'insert_validation' || $state == 'insert')
		{
			//Mandatory Feilds
			$crud->required_fields('operator_id', 'loginid', 'password', 'device_imie', 'route_id', 'access_status');
			
			$crud->field_type('created_by', 'hidden', $loggedinUser->username);
		
		
		}
		elseif ($state == 'edit' || $state == 'update_validation' || $state == 'update')
		{
			//Mandatory Feilds
			$crud->required_fields('password', 'device_imie', 'route_id', 'access_status');
			
				$crud->field_type('operator_id', 'readonly');
				$crud->field_type('loginid', 'readonly');
				
			$crud->field_type('modified_by', 'hidden', $loggedinUser->username);
		}
		
		// disable direct create / delete Frontend User
		//$crud->unset_add();
		//$crud->unset_delete();

		$this->mPageTitle = 'Operator Device Access';
		$this->render_crud();
	}
}
