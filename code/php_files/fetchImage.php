<?php
$con= new mysqli("localhost","animati1_admin","Wellowwellow%5","animati1_android");
if ($con->connect_error)
{
   die('Connect Error (' . $con->connect_errno . ') '
            . $con->connect_error);
}
$id = $_POST['id'];

$res="SELECT image FROM profile WHERE id='$id'";
$result = mysqli_query($con,$sql); 
$res = array(); 
while ($row = mysqli_fetch_array($result)){
        array_push($res, array
                          ("image"=>$row['image']));
    }

echo json_encode($res);
mysqli_close($con);
?>


