<?php

class SList extends Controller {

    public function __construct() {

        // a little list on that (seen on StackOverflow):
        // "As long as myChild has no constructor, the parent constructor will be called / inherited."
        // This means wenn a class thats extends another class has a __construct, it needs to construct
        // the parent in that constructor, like this:   
        parent::__construct();

        // VERY IMPORTANT: All controllers/areas that should only be useable by logged-in users
        // need this line! Otherwise not-logged in users could do actions
        // if all of your pages should only be useable by logged-in users: Put this line into
        // libs/Controller->__construct
        // TODO: test this!
        Auth::handleLogin();
    }

    public function index() 
    {
        // get all lists (of the logged in user)   
        if(!isset($_SESSION['active_list']))
            $this->model->getUserActive();
        $this->view->stores = $this->model->getAllStores();
        $this->view->active = $_SESSION['active_list'];
        $this->view->slists = $this->model->getAllLists();
        $this->view->items = $this->model->getAllItems();
        $this->view->errors = $this->model->errors;
        $this->view->render('slist/index');
    }
    
    public function getItemsPerStore($list_id)
    {
    	echo $this->model->getItemsPerStore($list_id);
    }

    public function create()
    {
        $this->model->create($_POST['list_name'], $_POST['store_id']);
        header('location: ' . URL . 'slist');
    }
    
    public function addToList($list_id, $item_id) {
   	$this->view->newitem = $this->model->addToList($list_id, $item_id);
        echo $this->view->newitem;
    }
    
    public function addSimpleToList($list_id) {
    	$this->view->newitem = $this->model->addSimpleToList($list_id, $_POST['item_name']);
        echo $this->view->newitem;
    }
    
    public function updateList() {
    	$this->model->updateList($_POST['list_values']);
    }

    public function edit($list_id)
    {
        $this->view->slist = $this->model->getList($list_id);
        $this->view->items = $this->model->getAllItems();
        $this->view->errors = $this->model->errors;
        $this->view->render('slist/edit');
    }

    public function editSave($list_id) {
        // do editSave() in the list_model, passing list_id from URL and list_text from POST via params
        $this->model->editSave($list_id, $_POST['list_text']);
        header('location: ' . URL . 'slist');
    }

    public function delete($list_id)
    {    
        $this->model->delete($list_id);
        header('location: ' . URL . 'slist');
    }
    
    public function deleteFromList($list_id)
    {
    	$this->model->deleteFromList($list_id, $_POST['item_pk'], $_POST['item_name']);
    }
    
    public function activeList($active_list)
    {
        $this->model->setActiveList($active_list);
    }

}