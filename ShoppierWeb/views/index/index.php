<div class="content">
    
    

    <?php

    if (isset($this->errors)) {

        foreach ($this->errors as $error) {
            echo '<div class="system_message">'.$error.'</div>';
        }

    }

    ?>
    <div class="row">
        <div class="col-md-3">
            <div class="well">
                <ul class="nav nav-list">
                    <li><a href="#Shoppier">What is Shoppier?<i class="icon-chevron-right"></i></a></li>
                    <li><a href="#lists">Shopping lists<i class="icon-chevron-right"></i></a></li>
                    <li><a href="#crowdsource">Crowdsourcing<i class="icon-chevron-right"></i></a></li>
                    <li><a href="#bestroute">Best Route<i class="icon-chevron-right"></i></a></li>
                    <li><a href="#aboutus">Who we are?<i class="icon-chevron-right"></i></a></li>
                </ul>
            </div>
        </div>
        <div class="col-md-9">
            <section id="Shoppier">
                <div class="page-header">
                    <h1>Shoppier: Shop Happier</h1>
                </div>
                <p>The idea behind Shoppier is an easy to use shopping cart integrated with store merchandise. People can look up actual items from stores and add them to shopping lists. The day they decide to go to the supermarket, shoppier will generate an ordered list of the best path to get all their items.</p>
            </section>
            <section id="lists">
                <div class="page-header">
                    <h1>Shopping Lists</h1>
                </div>
                <p>Create your own shopping lists from a database of West Michigan Stores! Type in your own items or select from a wide variety of items from many different stores
                located in the area.</p>

            </section>
            <section id="crowdsource">
                <div class="page-header">
                    <h1>Crowdsourcing</h1>
                </div>
                <h3>We've done some of the initial footwork and now we need you!</h3>
                <p>Members of shoppier get to enter items from any store available in the database!</p>
                <p>If you don't see an item you use regularly, just use the easy form on either our website or in our android app. Simply enter a name, brand, and choose the location of the item in the store and you are good to go. Items entered by you will be immediately available for addition to shopping lists and availabe for an item search</p>

            </section>
            <section id="aboutus">
                <div class="page-header">
                    <h1>About Us</h1>
                </div>
                <p>We are a team of GVSU students working on a Computer Science Senior Project.</p>
                <p>When visiting stores we realized that oftentimes we simply could not find items.
                 We thought it would be much easier to search the item on a database, rather than have
                 to track down a store attendant.  Thus Shoppier was born.</p>
            </section>
        </div>
    </div>
    
    
</div>
