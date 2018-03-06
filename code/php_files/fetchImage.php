<?php
$con= mysqli_connect("localhost","animati1_admin","Wellowwellow%5","animati1_android");
if ($con->connect_error)
{
   die('Connect Error (' . $con->connect_errno . ') '
            . $con->connect_error);
}
$id = $_GET['id'];

$res="SELECT image FROM profile WHERE id='$id'";
$result = mysqli_query($con,$res); 
$sql = array(); 
while ($row = mysqli_fetch_array($result)){
        array_push($sql, array
                          ("image"=>$row['image']));
    }

echo json_encode($sql);
mysqli_close($con);
?>


