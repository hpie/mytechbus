<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class VehicleRouteMatrix extends Admin_Controller {

	public function __construct()
	{
		parent::__construct();
		$this->load->library('form_builder');
	}

	// Frontend User CRUD
	public function index()
	{
		$loggedinUser = $this->mUser;
		$crud = $this->generate_crud('route_fare_matrix');
		$crud->columns('route_code', 'route_start_stage', 'route_end_stage', 'start_stage_code', 'end_stage_code', 'fare_km', 'fare_full', 'fare_half', 'fare_luggage', 'vehicle_type');
		
		
		//Show for add
		//$crud->add_fields('operator_id', 'route_code', 'stage_code', 'stage_name', 'stage_km', 'stage_no', 'stage_status');
		
		//Show only for Update
		//$crud->edit_fields('stage_code', 'stage_name', 'stage_km', 'stage_no', 'stage_status');
			
		//$crud->field_type('stage_status','dropdown',array('ACTIVE'=>'ACTIVE','INACTIVE'=>'INACTIVE','DELETED'=>'DELETED'));	

		//$crud->field_type('created_by', 'hidden', $loggedinUser->username);
		//$crud->field_type('modified_by', 'hidden', $loggedinUser->username);
		
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
		
		// disable direct create / delete Frontend User
		//$crud->unset_add();
		$crud->unset_delete();

		$this->mPageTitle = 'Operator Route - Stsge Fare';
		$this->render_crud();
	}
}

?>