<?php

function upload_resized_image($fn, $url)
{
    try {
        $size = getimagesize($fn);
        $ratio = $size[1] / $size[0]; // height/width
        if ($size[0] > 800) {
            $width = 800;
            $height = 800 * $ratio;
        } else {
            $width = $size[0];
            $height = $size[1];
        }
        $src = imagecreatefromstring(file_get_contents($fn));

        $dst = imagecreatetruecolor($width, $height);
        imagealphablending($dst, false);
        imagesavealpha($dst, true);

        imagecopyresampled($dst, $src, 0, 0, 0, 0, $width, $height, $size[0], $size[1]);
        imagedestroy($src);
        imagepng($dst, $url); // adjust format as needed
        imagedestroy($dst);
    } catch (Exception $e) {
    }
}
