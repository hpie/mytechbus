<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
<div class="container">

	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href=""><?php echo $site_name; ?></a>
	</div>
	
	<?php //echo "===========>>>>>>>>>>>> <pre>"; print_r($this->session->userdata('user_id')); exit; ?>
	<div class="navbar-collapse collapse">

		<ul class="nav navbar-nav">
			<?php 
			if ($this->session->userdata('user_id')) { ?>
				<li class="dropdown ">
					<a data-toggle="dropdown" class="dropdown-toggle" href="#">
						FrontDesk <span class="caret"></span>
					</a>
					<ul role="menu" class="dropdown-menu">
						<li class="dropdown-submenu">
							<a href="atc/enquiry">Enquiry</a>
						</li>
						<!-- <li class="dropdown-submenu">
							<a href="atc/atc_details">ATC details</a>
						</li>
						<li class="dropdown-submenu">
							<a href="atc/admission">Admit Student</a>
						</li>
						
						<li>
							<a href="atc/enroll">Enroll Student</a>
						</li>
						
						<li class="dropdown-submenu">
							<a class="submenu-link" href="#">Manage Couriers <span class="caret"></a>
							<ul class="dropdown-menu">
								<li class="dropdown-submenu">
									<a tabindex="-1" href="atc/courier">New Courier Entry</a>
								</li>
								<li class="dropdown-submenu">
									<a tabindex="-1" href="atc/couriers">Courier Details</a>
								</li>
							</ul>
						</li> -->
					</ul>
				</li>

				<li class="dropdown ">
					<a data-toggle="dropdown" class="dropdown-toggle" href="#">
						Views <span class="caret"></span>
					</a>
					<ul role="menu" class="dropdown-menu">
						<li class="dropdown-submenu">
							<a href="atc/enquiries">List Enquiries</a>
						</li>

						<!-- <li class="dropdown-submenu">
							<a class="submenu-link" href="#">Students <span class="caret"></a>
							<ul class="dropdown-menu">
								<li class="dropdown-submenu">
									<a tabindex="-1" href="atc/registrations">List Students</a>
								</li>
								<li class="dropdown-submenu">
									<a tabindex="-1" href="atc/documents">List Student Documents</a>
								</li>
								<li class="dropdown-submenu">
									<a tabindex="-1" href="atc/enrollments">List Enrollments</a>
								</li>
								<li class="dropdown-submenu">
									<a tabindex="-1" href="atc/studentassesment">List student assesment</a>
								</li>
							</ul>
						</li>
						
						<li class="dropdown-submenu">
							<a class="submenu-link" href="#">Requests <span class="caret"></a>
							<ul class="dropdown-menu">
								<li class="dropdown-submenu">
									<a tabindex="-1" href="atc/prospectus_requests">List Prospectus Requests</a>
								</li>
								<li class="dropdown-submenu">
									<a tabindex="-1" href="atc/list_book_requests">List Book Requests</a>
								</li> 
							</ul>
						</li>
						
						<li>
							<a href="atc/batches">List Batches</a>
						</li>
						<li>
							<a href="atc/courses">List Courses</a>
						</li>
						<li>
							<a href="atc/paylist">List Payments</a>
						</li> -->

					</ul>
				</li>

			<!-- <li class="dropdown ">
				<a data-toggle="dropdown" class="dropdown-toggle" href="#">
					Actions <span class="caret"></span>
				</a>
				<ul role="menu" class="dropdown-menu">
					<li class="dropdown-submenu">
						<a class="submenu-link" href="#">Requests <span class="caret"></a>
						<ul class="dropdown-menu">
							<li class="dropdown-submenu">
								<a tabindex="-1" href="atc/request_prospectus">Request Prospectus</a>
							</li>
							<li class="dropdown-submenu">
								<a tabindex="-1" href="atc/request_books">Request Books</a>
							</li>
							<li class="dropdown-submenu">
								<a tabindex="-1" href="#">Requests Certificates</a>
							</li>
						</ul>
					</li>
			
					<li class="dropdown-submenu">
						<a class="submenu-link" href="#">Students <span class="caret"></a>
						<ul class="dropdown-menu">
							<li class="dropdown-submenu">
								<a tabindex="-1" href="atc/book_issue_log">Issue Books</a>
							</li>
							<li class="dropdown-submenu">
								<a tabindex="-1" href="#">Issue Certificate</a>
							</li>
							<li class="dropdown-submenu">
								<a tabindex="-1" href="atc/student_assesment">Award Assesment Marks</a>
							</li>
						</ul>
					</li>
			
					<li class="dropdown-submenu">
						<a class="submenu-link" href="#">Payments <span class="caret"></a>
							<ul class="dropdown-menu">
								<li class="dropdown-submenu">
									<a tabindex="-1" href="atc/payment">Record Payment</a>
								</li> 
							</ul>
					</li>
			
					</ul>
			</li>
			
			
			<li class="dropdown ">
				<a data-toggle="dropdown" class="dropdown-toggle" href="#">
					Faculty <span class="caret"></span>
				</a>
				<ul role="menu" class="dropdown-menu">
					<li><a href="atc/faculty">Faculty Register</a></li>
					<li><a href="atc/facultyqualification">Faculty Qualification</a></li>
					<li><a href="atc/facultylist">List Faculty</a></li>
					<li><a href="atc/facultydocuments">List Faculty Documents</a></li>
					<li><a href="atc/couriers">Couriers</a></li>
					<li><a href="atc/attendance">Attendance</a></li>
			
				</ul>
			</li> -->


			<?php } if ($this->session->userdata('user_id')) {
			
			}if ($this->session->userdata('user_id')) {
			
			} if($this->session->userdata('identity') == 'student@gmail.com') {
			
			} if (!$this->session->userdata('user_id')) { ?>
				<li class="dropdown ">
					<a data-toggle="dropdown" class="dropdown-toggle" href="#">
						Login <span class="caret"></span>
					</a>
					<ul role="menu" class="dropdown-menu">
						<li><a href="auth/login">Login</a></li>
						<!-- <li><a href="auth/studentlogin">Student Login</a></li>
						<li><a href="auth/sign_up">Sign Up</a></li> -->
					</ul>
				</li>
			<?php } else { ?>
				<li>
					<a href="account/">
						My Account						
					</a>
				</li>
			<?php } ?>
			<li>
				<a href="operator/">
					Admin Login						
				</a>
			</li>
		</ul>

		<?php $this->load->view('_partials/language_switcher'); ?>
		
	</div>

</div>
</nav>