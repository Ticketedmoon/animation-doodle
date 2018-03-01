<?php
$con= new mysqli("localhost","animati1_admin","Wellowwellow%5","animati1_android");
if ($con->connect_error)
{
   die('Connect Error (' . $con->connect_errno . ') '
            . $con->connect_error);
}
$image = $_POST['imageFile'];
$video = $_POST['videoFile'];
$video_description = $_POST['videoDescription'];
$id = $_POST['id'];
$name = 'temp';
$rating = '2';
$result = "INSERT INTO videos2 (id,video_name,VIDEO, videoImage,videoRating,video_description) 
          VALUES ('$id','$name','$video','$image','$rating','$video_description')";
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



