<?php
	$action = htmlspecialchars($_POST['action']);
	$response = array("success" => FALSE);
	if($action == "photo") {
        $file_location = htmlspecialchars($_FILES['file']['tmp_name']);
        $file_name = htmlspecialchars($_FILES['file']['name']);
        $file_type = htmlspecialchars($_FILES['file']['type']);
        $file_size = htmlspecialchars($_FILES['file']['size']);
        $file_extension = strrchr($_FILES['file']['name'], ".");
        $valid_file_extensions = array(".jpg", ".jpeg", ".gif", ".png", ".JPG", ".JPEG");
        
        if(empty($file_location) && empty($file_name) && empty($file_type)){
	    	$response["success"] = FALSE;
			$response["message"] = "Upload Failed";
            
			echo json_encode($response);
            exit();
	    } else {
            if (@getimagesize($file_location) !== false) {
                if (in_array($file_extension, $valid_file_extensions)) {
                    move_uploaded_file($file_location, "photo/" . $file_name);
                    $response["success"] = TRUE;
                    $response["message"] = "Upload Successfull";
                }else{
                    $response["success"] = FALSE;
				    $response["message"] = "Type tidak didukung";
                }
            }else{
                $response["success"] = FALSE;
				$response["message"] = "Upload Failed";
            }
            
			echo json_encode($response);
            exit();
	    }
	}else if($action == "video") {
        $file_location = htmlspecialchars($_FILES['file']['tmp_name']);
        $file_name = htmlspecialchars($_FILES['file']['name']);
        $file_type = htmlspecialchars($_FILES['file']['type']);
        $file_size = htmlspecialchars($_FILES['file']['size']);
        $file_extension = strrchr($_FILES['file']['name'], ".");
        $valid_file_extensions = array(".mp4");
        
        if(empty($file_location) && empty($file_name) && empty($file_type)){
	    	$response["success"] = FALSE;
			$response["message"] = "Upload Failed 1";
            
			echo json_encode($response);
            exit();
	    } else {
            if (in_array($file_extension, $valid_file_extensions)) {
                move_uploaded_file($file_location, "video/" . $file_name);
                $response["success"] = TRUE;
                $response["message"] = "Upload Successfull";
            }else{
                $response["success"] = FALSE;
                $response["message"] = "Type tidak didukung";
            }
    		echo json_encode($response);
            exit();
	    }
    }
?>