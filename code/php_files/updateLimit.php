<?php
$con= new mysqli("localhost","animati1_admin","Wellowwellow%5","animati1_android");
if ($con->connect_error)
{
   die('Connect Error (' . $con->connect_errno . ') '
            . $con->connect_error);
}
$limit = $_POST['limit'];
$userId = $_POST['userId'];
$result = "UPDATE profile SET limitCounter ='$limit' WHERE id = '$userId'";
$sql = mysqli_query($con,$result);
if($sql == true) {
       echo '{"query_result":"SUCCESS"}';
   }
else{
       echo '{"query_result":"FAILURE"}';
       echo mysqli_error($con);
   }
mysqli_close($con);
?>
