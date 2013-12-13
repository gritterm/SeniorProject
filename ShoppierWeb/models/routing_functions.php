<?php

	function getList($listarr)
    {
    	
        $graph       = array();
        $edges       = array();
        $usedCat     = array();
        $newlistarr  = array();
        $notaddedarr = array();


        foreach($listarr as $item)
        {
        	if($item[6] == 0)
        	{
        		array_push($notaddedarr, $item[6]);
        	}
            else if(!in_array($item[6],$usedCat))
            {
                $vertex = new Vertex($item[6], $item[7], $item[8]);
                array_push($graph, $vertex);
                array_push($usedCat, $item[6]);
            }
            
        }
        $orderedlist = travelingSalesman($graph);
        foreach($orderedlist as $cat)
        {

        	foreach($listarr as $item)
        	{
        		if($item[6] == $cat)
        		{
        			array_push($newlistarr, $item);
        		}
        	}
        }
        
        if($newlistarr[0][6] > $newlistarr[count($newlistarr)-1][6])
        {
        	$newlistarr = array_reverse($newlistarr);
        }
        foreach($notaddedarr as $notrouted)
        {
        	foreach($listarr as $item)
        	{
        		if($item[6] == $notrouted)
        			array_push($newlistarr, $item);
        	}
        }
        return json_encode($newlistarr);
    }
    //this code was taken and modified from code by thomas hunter
    //the version of his code is freely available at 
    //https://thomashunter.name/blog/genetic-algorithm-traveling-salesperson-php/
	function travelingSalesman($graph)
	{
		define('CITY_COUNT', count($graph));
		$population = 50 + 0;
		$generations = 100 + 0;
		$elitism = 20 + 0;
		$names = array();
		$initialPopulation = array();
		$currentPopulation = array();
		$distances = array();

		# Take user city names and put it into an array
		for ($i = 0; $i < CITY_COUNT; $i++) {
			$names[$i] = $graph[$i]->getCat();
		}
		
		# Take user distance data and put it into a multidimensional array
		//I need to get this from my $graph array
		for ($i = 0; $i < CITY_COUNT; $i++) 
		{
			for ($j = 0; $j < CITY_COUNT; $j++) 
			{
				$val = count(getPath($graph[$i]->getX(), $graph[$i]->getY(), $graph[$j]->getX(), $graph[$j]->getY()));
				if($val < 0)
				{
					echo "in a wall";
				}
				$distances[$i][$j] = $val;
			}
		}
		
		# Building our initial population
		for($i = 0; $i < $population; $i++) {
			//returns a random abcedfg or cdbagfe etc.
			$initialPopulation[$i] = pickRandom(count($graph));
		}
		
		for ($k = 1; $k <= $generations; $k++) {
			//echo "<div><strong>Generation $k</strong></div>\n";
			# Rating population (I do some weird math to figure out their goodness level, not sure if it is good).
			//echo "<pre>";
			$i = 0;
			$distanceSum = 0;
			$biggest = 0;
			foreach ($initialPopulation AS $entity) {
				$currentPopulation[$i]['dna'] = $entity;
				$currentPopulation[$i]['rate'] = rate($entity, $distances);
				$distanceSum += $currentPopulation[$i]['rate'];
				if ($currentPopulation[$i]['rate'] > $biggest)
					$biggest = $currentPopulation[$i]['rate'];
				$i++;
			}
			$biggest += 1;
			$chancesSum = 0;
			for ($i = 0; $i < $population; $i++ ) {
				$currentPopulation[$i]['metric'] = $biggest - $currentPopulation[$i]['rate'];
				$chancesSum += $currentPopulation[$i]['metric'];
			}
			for ($i = 0; $i < $population; $i++ ) {
				$currentPopulation[$i]['chances'] = $currentPopulation[$i]['metric'] / $chancesSum;
			}
			util::sort($currentPopulation, 'rate');
			$ceilSum = 0;
			for ($i = 0; $i < $population; $i++ ) {
				$currentPopulation[$i]['floor'] = $ceilSum;
				$ceilSum += $currentPopulation[$i]['chances'];
			}
			//$this->debug($currentPopulation);
			//echo "</pre>\n";
			if (converging($initialPopulation))
				break;
			#Breeding time
			$initialPopulation = array();
			for ($j = 0; $j < $elitism; $j++) {
				$initialPopulation[] = $currentPopulation[$j]['dna'];
			}
			for ($j = 0; $j < $population - $elitism; $j++) {
				$rouletteMale = rand(0, 100) / 100;
				
				for ($i = $population - 1; $i >= 0; $i--) {
					if ($currentPopulation[$i]['floor'] < $rouletteMale) {
						$dad = $currentPopulation[$i]['dna'];
						break;
					}
				}
				
				$rouletteFemale = rand(0, 100) / 100;
				
				for ($i = $population - 1; $i >= 0; $i--) {
					if ($currentPopulation[$i]['floor'] < $rouletteFemale) {
						$mom = $currentPopulation[$i]['dna'];
						break;
					}
				}
				
				$child = mate($mom, $dad);
				$initialPopulation[] = $child;
			}
			
		}
		$arr = str_split($currentPopulation[0]['dna']);
		//echo "<div>The best solution I found is <strong>{"; 
		for($i = 0; $i<count($arr); $i++)
		{
			$arr[$i] = $graph[ord($arr[$i])-33]->getCat();
			//echo $graph[ord($val)-33]->getCat() ;

		}
		return $arr;
		//echo "</strong> with a mileage of <strong>".$this->rate($currentPopulation[0]['dna'], $distances)."</strong> which took <strong>$k</strong> generations.</div>\n";
	}
	function converging($pop) {
		$items = count(array_unique($pop));
		if ($items == 1)
			return true;
		else
			return false;
	}
	function pickRandom($count) {
		//think this needs to be names of my vertices
		$choices = array();
		for($i=0; $i<$count; $i++)
		{
			$choices[$i] = chr($i + 33);
		}
		//$choices = array('A', 'B', 'C', 'D', 'E', 'F', 'G');
		shuffle($choices);
		return implode('',$choices);
	}
	function num_to_letter($num, $uppercase = FALSE)
	{
		$num -= 1;
		
		$letter = 	chr(($num % 26) + 97);
		$letter .= 	(floor($num/26) > 0) ? str_repeat($letter, floor($num/26)) : '';
		return 		($uppercase ? strtoupper($letter) : $letter); 
	}
	function rate($dna, $distances) {
		$mileage = 0;
		$letters = str_split($dna);
		for ($i = 0; $i < CITY_COUNT - 1; $i++) {
			//echo $letters[$i];
			//echo $letters[$i+1];
			$part1 = ord($letters[$i])-33;
			$part2 = ord($letters[$i+1])-33;
			$row = $distances[$part1];
			$column = $row[$part2];
			$mileage += $distances[$part1][$part2];
		}
		return $mileage;
	}

	function debug($ar) {
		//echo "<table class='debug'>";
		//echo "<tr><th>&nbsp;</th><th>DNA</th><th>Fit</th><th>Roulette</th></tr>\n";
		foreach($ar as $element => $value) {
			//echo "<tr><td>" . $this->leadingZero($element) . "</td><td>" . $value['dna'] . "</td><td>" . $value['rate'] . "</td><td>" . sprintf("%01.2f", $value['chances'] * 100) . "%</td></tr>\n";
		}
		//echo "</table>\n";
	}

	function leadingZero($value) {
		if ($value < 10)
			$value = '00' . $value;
		else if ($value < 100)
			$value = '0' . $value;
		return $value;
	}

	function mate($mommy, $daddy) { # VERY INEFFICIENT! Combines genes randomly from both parents and if genes are repeated we do it again.
		$baby = "AAAAAA";
		$continue = true;
		$choices = array();
		for($i=0; $i<CITY_COUNT; $i++)
		{
			$choices[$i] = chr($i + 33);
		}
		//substr_count($baby, '!') != 1 || substr_count($baby, '#') != 1 || substr_count($baby, '$') != 1 || substr_count($baby, '"') != 1
		while ($continue) 
		{
			$count = 0;
			//|| substr_count($baby, 'E') != 1 || substr_count($baby, 'F') != 1 || substr_count($baby, 'G') != 1
			$baby = "";
			for($i = 0; $i < CITY_COUNT; $i++) {
				$chosen = mt_rand(0,1);
				if ($chosen)
					$baby .= substr($mommy, $i, 1);
				else
					$baby .= substr($daddy, $i, 1);
			}
			foreach($choices as $letter)
			{
				if(substr_count($baby, $letter) == 1)
				{
					$count = $count + 1;

				}
			}
			if($count == CITY_COUNT)
			{
				$continue = false;
			}
		}
		return $baby;
	}
	function getPath($pointAx, $pointAy, $pointBx, $pointBy)
	{
		$NodeGraph = new NodeGraph2D(14, 64);
		$NodeGraph->Set(Array(
			Array(0, 0, 255),
			Array(0, 1, 255),
			Array(0, 2, 255),
			Array(0, 3, 255),
			Array(0, 4, 255),
			Array(0, 5, 255),
			Array(0, 6, 255),
			Array(0, 7, 255),
			Array(0, 8, 255),
			Array(0, 9, 255),
			Array(0, 10, 255),
			Array(0, 11, 255),
			Array(0, 12, 255),
			Array(0, 13, 255),
			Array(1, 13, 255),
			Array(2, 13, 255),
			Array(3, 2, 255),
			Array(3, 3, 255),
			Array(3, 4, 255),
			Array(3, 5, 255),
			Array(3, 6, 255),
			Array(3, 7, 255),
			Array(3, 8, 255),
			Array(3, 9, 255),
			Array(3, 10, 255),
			Array(3, 13, 255),
			Array(4, 13, 255),
			Array(5, 13, 255),
			Array(6, 13, 255),
			Array(7, 13, 255),
			Array(8, 1, 255),
			Array(8, 2, 255),
			Array(8, 3, 255),
			Array(8, 4, 255),
			Array(8, 5, 255),
			Array(8, 6, 255),
			Array(8, 7, 255),
			Array(8, 8, 255),
			Array(8, 9, 255),
			Array(8, 10, 255),
			Array(8, 13, 255),
			Array(9, 13, 255),
			Array(10, 13, 255),

			Array(11, 4, 255),
			Array(11, 5, 255),
			Array(11, 6, 255),
			Array(11, 7, 255),
			Array(11, 8, 255),
			Array(11, 9, 255),
			Array(11, 10, 255),
			Array(11, 13, 255),
			Array(12, 13, 255),
			Array(13, 13, 255),

			Array(14, 4, 255),
			Array(14, 5, 255),
			Array(14, 6, 255),
			Array(14, 7, 255),
			Array(14, 8, 255),
			Array(14, 9, 255),
			Array(14, 10, 255),
			Array(14, 13, 255),
			Array(15, 13, 255),
			Array(16, 13, 255),

			Array(17, 1, 255),
			Array(17, 2, 255),
			Array(17, 3, 255),
			Array(17, 4, 255),
			Array(17, 5, 255),
			Array(17, 6, 255),
			Array(17, 7, 255),
			Array(17, 8, 255),
			Array(17, 9, 255),
			Array(17, 10, 255),
			Array(17, 13, 255),
			Array(18, 13, 255),
			Array(19, 13, 255),
			Array(20, 1, 255),
			Array(20, 2, 255),
			Array(20, 3, 255),
			Array(20, 4, 255),
			Array(20, 5, 255),
			Array(20, 6, 255),
			Array(20, 7, 255),
			Array(20, 8, 255),
			Array(20, 9, 255),
			Array(20, 10, 255),
			Array(20, 13, 255),
			Array(21, 13, 255),
			Array(22, 13, 255),

			Array(23, 1, 255),
			Array(23, 2, 255),
			Array(23, 3, 255),
			Array(23, 4, 255),
			Array(23, 5, 255),
			Array(23, 6, 255),
			Array(23, 7, 255),
			Array(23, 8, 255),
			Array(23, 9, 255),
			Array(23, 10, 255),
			Array(23, 13, 255),
			Array(24, 13, 255),
			Array(25, 13, 255),

			Array(26, 1, 255),
			Array(26, 2, 255),
			Array(26, 3, 255),
			Array(26, 4, 255),
			Array(26, 5, 255),
			Array(26, 6, 255),
			Array(26, 7, 255),
			Array(26, 8, 255),
			Array(26, 9, 255),
			Array(26, 10, 255),
			Array(26, 13, 255),
			Array(27, 13, 255),
			Array(28, 13, 255),

			Array(29, 1, 255),
			Array(29, 2, 255),
			Array(29, 3, 255),
			Array(29, 4, 255),
			Array(29, 5, 255),
			Array(29, 6, 255),
			Array(29, 7, 255),
			Array(29, 8, 255),
			Array(29, 9, 255),
			Array(29, 10, 255),
			Array(29, 13, 255),
			Array(30, 13, 255),
			Array(31, 13, 255),

			Array(32, 1, 255),
			Array(32, 2, 255),
			Array(32, 3, 255),
			Array(32, 4, 255),
			Array(32, 5, 255),
			Array(32, 6, 255),
			Array(32, 7, 255),
			Array(32, 8, 255),
			Array(32, 9, 255),
			Array(32, 10, 255),
			Array(32, 13, 255),
			Array(33, 13, 255),
			Array(34, 13, 255),

			Array(35, 1, 255),
			Array(35, 2, 255),
			Array(35, 3, 255),
			Array(35, 4, 255),
			Array(35, 5, 255),
			Array(35, 6, 255),
			Array(35, 7, 255),
			Array(35, 8, 255),
			Array(35, 9, 255),
			Array(35, 10, 255),
			Array(35, 13, 255),
			Array(36, 13, 255),
			Array(37, 13, 255),
			Array(38, 1, 255),
			Array(38, 2, 255),
			Array(38, 3, 255),
			Array(38, 4, 255),
			Array(38, 5, 255),
			Array(38, 6, 255),
			Array(38, 7, 255),
			Array(38, 8, 255),
			Array(38, 9, 255),
			Array(38, 10, 255),
			Array(38, 13, 255),
			Array(39, 13, 255),
			Array(40, 13, 255),

			Array(41, 1, 255),
			Array(41, 2, 255),
			Array(41, 3, 255),
			Array(41, 4, 255),
			Array(41, 5, 255),
			Array(41, 6, 255),
			Array(41, 7, 255),
			Array(41, 8, 255),
			Array(41, 9, 255),
			Array(41, 10, 255),
			Array(41, 13, 255),
			Array(42, 13, 255),
			Array(43, 13, 255),

			Array(44, 1, 255),
			Array(44, 2, 255),
			Array(44, 3, 255),
			Array(44, 4, 255),
			Array(44, 5, 255),
			Array(44, 6, 255),
			Array(44, 7, 255),
			Array(44, 8, 255),
			Array(44, 9, 255),
			Array(44, 10, 255),
			Array(44, 13, 255),
			Array(45, 13, 255),
			Array(46, 13, 255),

			Array(47, 1, 255),
			Array(47, 2, 255),
			Array(47, 3, 255),
			Array(47, 4, 255),
			Array(47, 5, 255),
			Array(47, 6, 255),
			Array(47, 7, 255),
			Array(47, 8, 255),
			Array(47, 9, 255),
			Array(47, 10, 255),
			Array(47, 13, 255),
			Array(48, 13, 255),
			Array(49, 13, 255),

			Array(50, 1, 255),
			Array(50, 2, 255),
			Array(50, 3, 255),
			Array(50, 4, 255),
			Array(50, 5, 255),
			Array(50, 6, 255),
			Array(50, 7, 255),
			Array(50, 8, 255),
			Array(50, 9, 255),
			Array(50, 13, 255),
			Array(53, 1, 255),
			Array(53, 2, 255),
			Array(53, 3, 255),
			Array(53, 4, 255),
			Array(53, 5, 255),
			Array(53, 6, 255),
			Array(53, 7, 255),
			Array(53, 8, 255),
			Array(51, 13, 255),
			Array(52, 13, 255),
			Array(53, 13, 255),
			Array(56, 10, 255),

			Array(58, 13, 255),
			Array(57, 10, 255),

			Array(59, 13, 255),
			Array(58, 10, 255),

			Array(60, 13, 255),
			Array(59, 10, 255),
			Array(57, 1, 255),
			Array(57, 5, 255),
			Array(58, 6, 255),
			Array(59, 7, 255),
			Array(58, 2, 255),
			Array(59, 3, 255),
			Array(61, 13, 255),
			Array(60, 10, 255),
			Array(57, 13, 255),
			Array(56, 13, 255),
			Array(55, 13, 255),
			Array(54, 13, 255),
			Array(62, 13, 255),
			Array(61, 10, 255),

			Array(63, 0, 255),
			Array(63, 1, 255),
			Array(63, 2, 255),
			Array(63, 3, 255),
			Array(63, 4, 255),
			Array(63, 5, 255),
			Array(63, 6, 255),
			Array(63, 7, 255),
			Array(63, 8, 255),
			Array(63, 9, 255),
			Array(63, 10, 255),
			Array(63, 11, 255),
			Array(63, 12, 255),
			Array(63, 13, 255)	
			));
		
		$Start = $NodeGraph->XY2Node($pointAx, $pointAy);
		$End = $NodeGraph->XY2Node($pointBx, $pointBy);
	//while($NodeGraph->H($Start, $End) < 25)
	//	$End = $NodeGraph->Random();
	
	
		$t0 = microtime(true);
		$PathFinder = new PathFinding($NodeGraph);
		$t1 = microtime(true);
		$Path = $PathFinder->Find($Start, $End);
		$t2 = microtime(true);
		return $Path;
	}



	class util {
    static private $sortfield = null;
    static private $sortorder = 1;
    static private function sort_callback(&$a, &$b) {
        if($a[self::$sortfield] == $b[self::$sortfield]) return 0;
        return ($a[self::$sortfield] < $b[self::$sortfield])? -self::$sortorder : self::$sortorder;
    }
    static function sort(&$v, $field, $asc=true) {
        self::$sortfield = $field;
        self::$sortorder = $asc? 1 : -1;
        usort($v, array('util', 'sort_callback'));
    }
}
class vertex
{

     
        private $loc;
        private $locx; 
        private $locy;
        function __construct($loc, $x, $y)
        {
            if($x == null)
                $x = -1;
            if($y == null)
                $y = -1;
            $this->locx = $x;
            $this->locy = $y;
            $this->loc = $loc;
        }
        function setPrevious($vertex)
        {
            $this->previous = $vertex;
        }
        function addNeighbor($vertex)
        {
            array_push($this->neighbors, $vertex);
        }
        function setDistance($distance)
        {
            $this->distance = $distance;
        }
        function getX()
        {
            return $this->locx;
        }
        function getY()
        {
            return $this->locy;
        }
        function getCat()
        {
            return $this->loc;
        }
}






?>