<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Home page
 */
class Track extends Admin_Controller {
	
	public function __construct()
	{
		parent::__construct();

		$this->load->helper('url');
		$this->load->library('form_builder');
	}

	public function index()
	{
		$form = $this->form_builder->create_form();

		// get list of Frontend user groups
		//$this->load->model('group_model', 'groups');
		//$this->mViewData['groups'] = $this->groups->get_all();
		$this->mPageTitle = 'Track Bus';

		//$this->mViewData['form'] = $form;
		$this->render('track');
	}

	public function bus()
	{
		$form = $this->form_builder->create_form();

		$loggedinUser = $this->mUser;

		$all_vehicles = $this->db->get_where('vehicle_master', array('operator_id' => $loggedinUser->operator_id))->result();
		$this->mPageTitle = 'Track Bus';

		$this->mViewData['all_vehicles'] = $all_vehicles;
		$this->render('track');
	}

	public function get_location() {

		if($this->input->post('route') != '' || $this->input->post('bus') != '') {
			$this->db->select('*');
			$this->db->from('location_log');

			if($this->input->post('route') != '') {
				$this->db->where('route_code', $this->input->post('route'));
			} else if($this->input->post('bus') != '') {
				$this->db->where('vehical_code', $this->input->post('bus'));
			}		
			
			$this->db->limit(1);
			$this->db->order_by('row_id','DESC');

			$location_data = $this->db->get()->row();

			if(count($location_data) > 0) {
				$response = array(
					'status'	=> 1,
					'message'	=> 'Record found.',
					'latitude'		=> $location_data->latitude,
					'longitude'		=> $location_data->longitude,
					'query'		=> $this->db->last_query()
				);
			} else {
				$response = array(
					'status'	=> 0,
					'message'	=> 'No record found.',
					'query'		=> $this->db->last_query()
				);
			}
		} else {
			$response = array(
				'status'	=> 0,
				'message'	=> 'Please enter bus code or route code.'
			);
		}

		echo json_encode($response);
	}
}
