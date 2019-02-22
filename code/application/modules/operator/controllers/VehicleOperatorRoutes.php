<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class VehicleOperatorRoutes extends Admin_Controller {

	public function __construct()
	{
		parent::__construct();
		$this->load->library('form_builder');


		
	}

	// Frontend User CRUD
	public function index()
	{
		$loggedinUser = $this->mUser;

		$crud = $this->generate_crud('master_routes');
		
        $crud->where('operator_id', $loggedinUser->operator_id);
		$crud->columns('operator_id', 'route_code', 'route_start_stage', 'route_end_stage', 'route_stop_count', 'route_type', 'route_status');
		
		
		//Show for add
		$crud->add_fields('operator_id', 'route_code', 'route_start_stage', 'route_end_stage', 'route_type', 'route_status');
		
		//Show only for Update
		
		$crud->edit_fields('operator_id', 'route_code', 'route_start_stage', 'route_end_stage', 'route_type', 'route_status');
		
		$crud->field_type('route_type','dropdown',array('TWOWAY'=>'TWOWAY','CIRCULAR'=>'CIRCULAR'));
		$crud->field_type('route_status','dropdown',array('ACTIVE'=>'ACTIVE','INACTIVE'=>'INACTIVE','DELETED'=>'DELETED'));	

		
		
		//how to add others? and create a new record if others
			if ($crud->getState()=='list')
		{
		$crud->set_relation('operator_id','vehicle_operators','{operator_name}-{operator_city}',array('operator_status' => 'ACTIVE'), 'operator_name ASC');
		}
		
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
			$crud->required_fields('operator_id', 'route_code', 'route_start_stage', 'route_end_stage', 'route_type', 'route_status');
					
			$crud->field_type('operator_id', 'hidden', $loggedinUser->operator_id);
			$crud->field_type('created_by', 'hidden', $loggedinUser->username);
		
		
		}
		elseif ($state == 'edit' || $state == 'update_validation' || $state == 'update')
		{
			//Mandatory Feilds
			$crud->required_fields('route_start_stage', 'route_end_stage', 'route_type', 'route_status');
			
				$crud->field_type('operator_id', 'hidden', $loggedinUser->operator_id);
				$crud->field_type('route_code', 'readonly');
				
			$crud->field_type('modified_by', 'hidden', $loggedinUser->username);
		}
		
		
		// disable direct create / delete Frontend User
		//$crud->unset_add();
		$crud->unset_delete();

		$this->mPageTitle = 'Operator Routes';
		$this->render_crud();
	}
}
