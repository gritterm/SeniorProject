<?php

class Route extends Controller {

	function __construct() {
            
		parent::__construct();

		Auth::handleLogin();

	}
	function index($route)
	{
		$this->view->list =  $this->model->getOrderedList($route);
		$this->view->route = $route;
		$this->view->render('route/index');
	}

}