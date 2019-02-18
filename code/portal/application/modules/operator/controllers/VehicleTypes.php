<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class VehicleTypes extends Admin_Controller {

	public function __construct()
	{
		parent::__construct();
		$this->load->library('form_builder');
	}

	// Frontend User CRUD
	public function index()
	{
		$loggedinUser = $this->mUser;
		$crud = $this->generate_crud('vehicle_types');
		$crud->columns('vehicle_type', 'vehicle_desc', 'vehicle_basefare', 'vehicle_type_status');
		
		
		//Show for add
		$crud->add_fields('vehicle_type', 'vehicle_desc','vehicle_basefare', 'vehicle_type_status');
		
		//Show only for Update
		$crud->edit_fields('vehicle_type', 'vehicle_desc','vehicle_basefare', 'vehicle_type_status');
			
		$crud->field_type('vehicle_type_status','dropdown',array('ACTIVE'=>'ACTIVE','INACTIVE'=>'INACTIVE','DELETED'=>'DELETED'));	

		
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
			$crud->required_fields('vehicle_type', 'vehicle_desc', 'vehicle_basefare', 'vehicle_type_status');
			
			$crud->field_type('created_by', 'hidden', $loggedinUser->username);
		
		
		}
		elseif ($state == 'edit' || $state == 'update_validation' || $state == 'update')
		{
			//Mandatory Feilds
			$crud->required_fields('vehicle_desc', 'vehicle_basefare', 'vehicle_type_status');
			
				$crud->field_type('vehicle_type', 'readonly');
				//$crud->field_type('device_imie', 'readonly');
				
			$crud->field_type('modified_by', 'hidden', $loggedinUser->username);
		}
		
		// disable direct create / delete Frontend User
		//$crud->unset_add();
		//$crud->unset_delete();

		$this->mPageTitle = 'Vehicle Types';
		$this->render_crud();
	}
}
