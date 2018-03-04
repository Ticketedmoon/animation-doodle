<?php
$con= new mysqli("localhost","animati1_admin","Wellowwellow%5","animati1_android");
if ($con->connect_error)
{
   die('Connect Error (' . $con->connect_errno . ') '
            . $con->connect_error);
}
$text = $_POST['text'];
$id = $_POST['id'];
$result2 = "UPDATE profile SET temp = '$text' WHERE id = '$id'";
$sql = mysqli_query($con,$result2);
if($sql == true) {
       echo '{"query_result":"SUCCESS"}';
   }
else{
       echo '{"query_result":"FAILURE"}';
       echo mysqli_error($con);
   }
mysqli_close($con);
?>
