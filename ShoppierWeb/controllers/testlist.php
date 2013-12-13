<?php

class TestList extends Controller {

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
        $this->view->json1 = $this->model->addToList(86, 7);
        $this->view->list1 = $this->model->getList(86);
        $this->view->json2 = $this->model->addToList(86, 8);

        $this->view->list2 = $this->model->getList(86);
        $this->view->json3 = $this->model->addToList(86, 9);

        $this->model->deleteFromList(86, 8);
        $this->view->list3 = $this->model->getList(86);
        $this->view->render('testlist/index');


    }

}