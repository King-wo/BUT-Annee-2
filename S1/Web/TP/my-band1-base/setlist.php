<?php
require('header.php');

$columns = array('title','artist','style');
$column = isset($_GET['column']) && in_array($_GET['column'], $columns) ? $_GET['column'] : $columns[0];
$sort_order = isset($_GET['order']) && strtolower($_GET['order']) == 'desc' ? 'DESC' : 'ASC';

$up_or_down = str_replace(array('ASC','DESC'), array('up','down'), $sort_order); 
$asc_or_desc = $sort_order == 'ASC' ? 'desc' : 'asc';
$add_class = ' class="highlight"';

$sql = "SELECT * from setlist ORDER BY " .  $column . " " . $sort_order;

$connexion=dbconnect(); 
if(!$connexion->query($sql)) {
  echo "Pb d'accès à la bdd"; 
}
else{ 
    

  ?> 

  <div class="main">
  <!-- Titre -->
    <header class="intro">
        <h1> Set List </h1>
    </header>

    

    <div class="row">
        <div class="col-sm">
            <table id="songTable" style="width:90%;margin: auto;">
                <thead>
                    <tr>
                        <th class="headersort"><a href="./setlist.php?column=title&order=<?php echo $asc_or_desc; ?>">TITLE <i class="fas fa-sort<?php echo $column == 'title' ? '-' . $up_or_down : ''; ?>"></i></a></th>
                        <th class="headersort"><a href="./setlist.php?column=artist&order=<?php echo $asc_or_desc; ?>">ARTIST(S) <i class="fas fa-sort<?php echo $column == 'artist' ? '-' . $up_or_down : ''; ?>"></i></a></th>
                        <th class="headersort"><a href="./setlist.php?column=style&order=<?php echo $asc_or_desc; ?>">STYLE <i class="fas fa-sort<?php echo $column == 'style' ? '-' . $up_or_down : ''; ?>"></i></a></th>
                        

                    </tr>
                </thead>
                <tbody>
                <?php 
                    foreach ($connexion->query($sql) as $row) {
                        echo "<tr><td>".$row['title']."</td> <td>".
                                        $row['artist']."</td> <td>".
                                        $row['style']."</td></tr>";
                    }
                ?> 
                </tbody>
            </table>
        </div>
    </div>


</div>
  
<?php
}


require('footer.php');
?>