<?php

class Additem extends Controller {

	function __construct() {
            
		parent::__construct();

	}
	
	function index() {
        $this->view->stores = $this->model->getAllStores();    
		$this->view->render('additem/index');	

	}
	function aisles()
	{
		$this->view->aisles = $this->model->getAisles($_POST['store']);
		echo $this->view->aisles;
	}
	function locations()
	{
		$this->view->locations = $this->model->getLocations($_POST['aisle']);
		echo $this->view->locations;
	}
	function add()
	{
		$this->model->add($_POST['item']);
	}

}