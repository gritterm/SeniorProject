<?php

class Testroute extends Controller {

	function __construct() {
            
		parent::__construct();

		Auth::handleLogin();

	}
	function index($route)
	{

		$this->view->list =  $this->model->getOrderedList($route);
		$this->view->render('testroute/index');
	}
	
	function route($list)
	{
		$this->view->orderedlist = $this->model->getOrderedList($list);
		$this->view->render('testroute/index');
		
	}
	

}