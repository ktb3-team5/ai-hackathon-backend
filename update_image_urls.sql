-- 미디어 이미지 URL 업데이트
USE KTB_DB;

UPDATE tb_media SET poster_url = '/images/media/1.webp' WHERE id = 1;
UPDATE tb_media SET poster_url = '/images/media/2.jpg' WHERE id = 2;
UPDATE tb_media SET poster_url = '/images/media/3.jpeg' WHERE id = 3;
UPDATE tb_media SET poster_url = '/images/media/4.JPG' WHERE id = 4;
UPDATE tb_media SET poster_url = '/images/media/5.JPG' WHERE id = 5;
UPDATE tb_media SET poster_url = '/images/media/6.JPG' WHERE id = 6;
UPDATE tb_media SET poster_url = '/images/media/7.JPG' WHERE id = 7;
UPDATE tb_media SET poster_url = '/images/media/8.JPG' WHERE id = 8;
UPDATE tb_media SET poster_url = '/images/media/9.JPG' WHERE id = 9;
UPDATE tb_media SET poster_url = '/images/media/10.JPG' WHERE id = 10;

-- 여행지 이미지 URL 업데이트
UPDATE tb_destination SET image_url = '/images/destinations/1.png' WHERE id = 1;
UPDATE tb_destination SET image_url = '/images/destinations/2.png' WHERE id = 2;
-- 나머지 여행지들도 추가 필요

-- 확인
SELECT id, title, poster_url FROM tb_media;
SELECT id, name, image_url FROM tb_destination LIMIT 10;
