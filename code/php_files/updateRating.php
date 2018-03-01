<?php
$con= new mysqli("localhost","animati1_admin","Wellowwellow%5","animati1_android");
if ($con->connect_error)
{
   die('Connect Error (' . $con->connect_errno . ') '
            . $con->connect_error);
}
$rating = $_POST['rating'];
$video_name = $_POST['video'];
$ratingCounter = $_POST['ratingCounter'];
$result = "UPDATE videos2 SET videoRating ='$rating', ratingCounter = '$ratingCounter' WHERE VIDEO = '$video_name'";
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
