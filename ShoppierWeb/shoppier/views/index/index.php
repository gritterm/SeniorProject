<div class="content">
    
    <h1>Index</h1>

    <?php

    if (isset($this->errors)) {

        foreach ($this->errors as $error) {
            echo '<div class="system_message">'.$error.'</div>';
        }

    }

    ?>
    
    <p>
        This box (everything between header and footer) is the content of views/index/index.php, so it's the index/index view.
        <br/>
        It's rendered by the index-method within the index-controller (in controllers/index.php).
    </p>
    
</div>
