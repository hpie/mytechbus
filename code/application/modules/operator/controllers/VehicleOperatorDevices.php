<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class VehicleOperatorDevices extends Admin_Controller {

	public function __construct()
	{
		parent::__construct();
		$this->load->library('form_builder');
	}

	// Frontend User CRUD
	public function index()
	{
		$loggedinUser = $this->mUser;
		$crud = $this->generate_crud('vehicle_operator_devices');
		
        $crud->where('operator_id', $loggedinUser->operator_id);
		$crud->columns('operator_id', 'device_imie', 'device_make', 'device_model', 'device_os', 'device_osversion', 'device_osversion', 'device_osversion', 'device_network', 'device_number', 'device_status');
		
		
		//Show for add
		$crud->add_fields('device_imie', 'device_make', 'device_model', 'device_os', 'device_osversion', 'device_osversion', 'device_osversion', 'device_network', 'device_number', 'device_status', 'operator_id');
		
		//Show only for Update
		$crud->edit_fields('operator_id', 'device_imie', 'device_make', 'device_model', 'device_os', 'device_osversion', 'device_osversion', 'device_osversion', 'device_network', 'device_number', 'device_status');
			
		$crud->field_type('device_status','dropdown',array('ACTIVE'=>'ACTIVE','INACTIVE'=>'INACTIVE','DELETED'=>'DELETED'));	

		
		//how to add others? and create a new record if others
		//$crud->set_relation('operator_id','vehicle_operators','{operator_name}-{operator_city}',array('operator_status' => 'ACTIVE'), 'operator_name ASC');
		
		//how to add others? and create a new record if others
		//$crud->set_relation('route_code','master_routes','{route_start_stage}-{route_end_stage}',array('route_status' => 'ACTIVE'), 'route_code, route_start_stage ASC');
		
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
			$crud->required_fields('device_imie', 'device_number', 'device_status');
			
			$crud->field_type('operator_id', 'hidden', $loggedinUser->operator_id);
			
			$crud->field_type('created_by', 'hidden', $loggedinUser->username);
		
		
		}
		elseif ($state == 'edit' || $state == 'update_validation' || $state == 'update')
		{
			//Mandatory Feilds
			$crud->required_fields('device_number', 'device_status');
			
				$crud->field_type('operator_id', 'hidden', $loggedinUser->operator_id);
				$crud->field_type('device_imie', 'readonly');
				
			$crud->field_type('modified_by', 'hidden', $loggedinUser->username);
		}
		
		// disable direct create / delete Frontend User
		//$crud->unset_add();
		//$crud->unset_delete();

		$this->mPageTitle = 'Operator Devices';
		$this->render_crud();
	}
}