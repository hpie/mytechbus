<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Home extends Admin_Controller {

	public function index()
	{
		$loggedinUser = $this->mUser;
		$this->load->model('user_model', 'users');

		$users_count = $this->db->get_where('users', array('operator_id' => $loggedinUser->operator_id))->num_rows();
		$this->mViewData['count'] = array(
			//'users' => $this->users->count_all(),
			'users' => $users_count,
		);
		$this->render('home');
	}
}
