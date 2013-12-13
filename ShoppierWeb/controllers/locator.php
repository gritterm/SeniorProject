<?php

class Locator extends Controller {

	function __construct() {
            
		parent::__construct();

	}
	
	function index() {
        $this->view->items = $this->model->getAllItems();
		$this->view->render('locator/index');	

	}
	function getLoc(){
		$this->view->location = $this->model->drawLocation($_POST['itemid']);
		echo $this->view->location;
	}

}
