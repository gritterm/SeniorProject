 <?php

class TestList_Model extends Model
{
    public $errors = array();
    public function __construct()
    {
        parent::__construct();
    }
  public function getList($list_id)
    {
        
        $sth = $this->db->prepare("SELECT *
                 FROM list
                 WHERE list_userfk = :user_id AND list_pk = :list_id;");
        $sth->execute(array(
            ':user_id' => $_SESSION['user_id'],
            ':list_id' => $list_id));    

        return $sth->fetch()->list_items;
    }
     public function deleteFromList($list_id, $item_pk)
    {
        $sth = $this->db->prepare("SELECT * 
                                   FROM list
                                   WHERE list_pk = :list;");
        $sth->execute(array(
            ':list' => $list_id
            ));
        $listjson = $sth->fetch()->list_items;
        $list = json_decode($listjson);
        $count = 0;
        foreach($list as $row)
        {

          if($row[1] == $item_pk)
            array_splice($list, $count, count($list), array_slice($list, $count+1));

          else
            $count = $count + 1;

        }
        $newlist = json_encode($list);
        $qth = $this->db->prepare("UPDATE list SET list_items = :list where list_pk = :listpk;");
        $qth->execute(array(
            ':list' => $newlist,
            ':listpk' => $list_id
        ));

    }
   public function addToList($list_id, $item_pk)
     {
        //get the list_items from the database
        $rth = $this->db->prepare("SELECT *
                                   FROM list
                                   WHERE list_pk = :list;");
        $rth->execute(array(
            ':list' => $list_id
            ));
        $listrow = $rth->fetch();

        $json = $listrow->list_items;
        $list = json_decode($json);
        $inlist = false;
        $rowcount = 0;
        for($i = 0; $i < count($list); $i++)
        {
            if($list[$i][1] == $item_pk)
            {
                $inlist = true;
                $list[$i][5] = $list[$i][5] + 1;
            }
        }
        if($inlist == false)
        {
            //get item information to add to the list
            $sth = $this->db->prepare("SELECT 0 as checked, item_pk, item_name, item_brand, 0 as price, 0 as quantity,
                                              cat_value, cat_x, cat_y 
                                       FROM item
                                       INNER JOIN category on cat_pk = item_catfk
                                       WHERE item_pk = :item;");
            $sth->execute(array(
                ':item' => $item_pk
                ));
            $itemrow = $sth->fetch();

            if(count($list)<1)
              $list = array();
            $newitem = array();
            foreach($itemrow as $column)
            {
              array_push($newitem, $column);
            }
            $newitemjson = json_encode($newitem);
            array_push($list, $newitem);
        }
        $listjson = json_encode($list);
        $qth = $this->db->prepare("UPDATE list SET list_items = :list where list_pk = :listpk;");
        $qth->execute(array(
            ':list' => $listjson,
            ':listpk' => $list_id
          ));
        if($inlist == false)
          return $newitemjson;
        else
          return "didn't add";
        
    }
}