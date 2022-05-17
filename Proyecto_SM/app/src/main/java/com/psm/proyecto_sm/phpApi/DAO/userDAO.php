<?php

require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/userModel.php';
require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/postModel.php';
require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/replyModel.php';
require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/config.php';

class UsuarioDAO{

    private $connection;

    public function __construct(){
        $database = new BaseDeDatos;
        $this->connection = $database->connect();
    }

    public function __destruct(){
        $this->connection = null;
    }

    public function readUser($us){
        $listaUsuarios = [];
        
        try{
            $sql = 'SELECT * FROM USERS WHERE id_user = ?';

            $statement = $this->connection->prepare($sql);
            $statement->bindParam(1,$us);
            $statement->execute();

            while ($row = $statement->fetch(PDO::FETCH_ASSOC)){
                
                $id_user = $row['id_user'];
                $name = $row['name'];
                $email = $row['email'];
                $password = $row['password'];
                $address = $row['address'];
                $phone = $row['phone'];
                $register_date = $row['register_date'];
                $profilePic = $row['profilePic'];


                $user = new UserModel();
                $user->addUser($id_user, $name, $profilePic, $email, $password, $phone,$address,$register_date);
                $listaUsuarios[] = $user;

            }
        }
        catch(PDOException $e){
            error_log($e->getMessage());
        }
        finally{
            $statement->closeCursor();
        }

        return $listaUsuarios;
    }

    public function readUserDraftPosts($us){
        $listaPosts = [];
        //Traemos el usuario y su foto con la funcion readUser y lo guardamos en user_name y user_pic para mandarlo en listaPosts
        $listaU[] = readUser($us);

        $user_name = $listaU->name;
        $user_pic = $listaU->profilePic;
        
        try{
            $sql = 'SELECT * FROM DRAFTS WHERE id_user = ?';
            //Hacemos este select para traernos las id de los posts del usuario y luego hacemos un while para ir pasando uno por uno
            $statement = $this->connection->prepare($sql);
            $statement->bindParam(1,$us);
            $statement->execute();

            while ($row = $statement->fetch(PDO::FETCH_ASSOC)){
                
                $id_post = $row['id_post'];
                $id_user = $row['id_user'];
                $title = $row['title'];
                $content = $row['content'];
                $favorites = $row['favorites'];
                $isDraft = $row['isDraft'];
                $posted_date = $row['posted_date'];
                $image1 = $row['image1'];
                $image2 = $row['image2'];
                $user_name = $row['user_name'];
                $user_pic = $row['user_pic'];

                $posts = new PostModel();
                $posts->addPost($id_post, $id_user, $title, $content, $favorites, $isDraft,$posted_date,$image1,$image2,$user_name,$user_pic);
                $listaPosts[] = $posts;

            }
        }
        catch(PDOException $e){
            error_log($e->getMessage());
        }
        finally{
            $statement->closeCursor();
        }

        return $listaPosts;
    }

    public function readUserFavorites($us){
        $listaPosts = [];
        //Traemos el usuario y su foto con la funcion readUser y lo guardamos en user_name y user_pic para mandarlo en listaPosts
        $listaU[] = readUser($us);

        $user_name = $listaU->name;
        $user_pic = $listaU->profilePic;
        
        try{
            $sql = 'SELECT * FROM FAVS WHERE id_user = ?';
            //Hacemos este select para traernos las id de los posts del usuario y luego hacemos un while para ir pasando uno por uno
            $statement = $this->connection->prepare($sql);
            $statement->bindParam(2,$us);//ver si lo que mando es solo un id o toda la clase usuario
            $statement->execute();

            while ($row = $statement->fetch(PDO::FETCH_ASSOC)){
                
                $id_post = $row['id_post'];
                $sql2 = 'SELECT * FROM POSTS WHERE id_post = ?'
                $statement2 = $this->connection->prepare($sql);
                $statement2->bindParam(1,$id_post);
                $statement2->execute();
                //hacemos otro statement para traernos la info del post en el que se encuentra el while
                while ($row2 = $statement2->fetch(PDO::FETCH_ASSOC)){
                    $id_post = $row2['id_post'];
                    $id_user = $row2['id_user'];
                    $title = $row2['title'];
                    $content = $row2['content'];
                    $favorites = $row2['favorites'];
                    $isDraft = $row2['isDraft'];
                    $posted_date = $row2['posted_date'];
                    $image1 = $row2['image1'];
                    $image2 = $row2['image2'];
                    $un = $user_name;
                    $up = $user_pic;

                    $posts = new PostModel();
                    $posts->addPost($id_post, $id_user, $title, $content, $favorites, $isDraft,$posted_date,$image1,$image2,$user_name,$user_pic);
                    $listaPosts[] = $posts;
                }

            }
        }
        catch(PDOException $e){
            error_log($e->getMessage());
        }
        finally{
            $statement->closeCursor();
        }

        return $listaPosts;
    }

    public function readUserPosts($us){
        $listaPosts = [];
        //Traemos el usuario y su foto con la funcion readUser y lo guardamos en user_name y user_pic para mandarlo en listaPosts
        $listaU[] = readUser($us);

        $user_name = $listaU->name;
        $user_pic = $listaU->profilePic;
        
        try{
            $sql = 'SELECT * FROM POSTS WHERE id_user = ?';
            //Hacemos este select para traernos las id de los posts del usuario y luego hacemos un while para ir pasando uno por uno
            $statement = $this->connection->prepare($sql);
            $statement->bindParam(1,$us->Id_Usuario);
            $statement->execute();

            while ($row = $statement->fetch(PDO::FETCH_ASSOC)){
                
                $id_post = $row['id_post'];
                $id_user = $row['id_user'];
                $title = $row['title'];
                $content = $row['content'];
                $favorites = $row['favorites'];
                $isDraft = $row['isDraft'];
                $posted_date = $row['posted_date'];
                $image1 = $row['image1'];
                $image2 = $row['image2'];
                $user_name = $row['user_name'];
                $user_pic = $row['user_pic'];

                $posts = new PostModel();
                $posts->addPost($id_post, $id_user, $title, $content, $favorites, $isDraft,$posted_date,$image1,$image2,$user_name,$user_pic);
                $listaPosts[] = $posts;

            }
        }
        catch(PDOException $e){
            error_log($e->getMessage());
        }
        finally{
            $statement->closeCursor();
        }

        return $listaPosts;
    }

    public function readUserReplies($us){
        $listaPosts = [];
        //Traemos el usuario y su foto con la funcion readUser y lo guardamos en user_name y user_pic para mandarlo en listaPosts
        $listaU[] = readUser($us);

        $user_name = $listaU->name;
        $user_pic = $listaU->profilePic;
        
        try{
            $sql = 'SELECT * FROM REPLIES WHERE id_user = ?';
            //Hacemos este select para traernos las id de los posts del usuario y luego hacemos un while para ir pasando uno por uno
            $statement = $this->connection->prepare($sql);
            $statement->bindParam(2,$us->Id_Usuario);
            $statement->execute();

            while ($row = $statement->fetch(PDO::FETCH_ASSOC)){
                
                $id_post = $row['id_post'];
                $sql2 = 'SELECT * FROM POSTS WHERE id_post = ?'
                $statement2 = $this->connection->prepare($sql);
                $statement2->bindParam(1,$id_post);
                $statement2->execute();
                //hacemos otro statement para traernos la info del post en el que se encuentra el while
                while ($row2 = $statement2->fetch(PDO::FETCH_ASSOC)){
                    $id_post = $row2['id_post'];
                    $id_user = $row2['id_user'];
                    $title = $row2['title'];
                    $content = $row2['content'];
                    $favorites = $row2['favorites'];
                    $isDraft = $row2['isDraft'];
                    $posted_date = $row2['posted_date'];
                    $image1 = $row2['image1'];
                    $image2 = $row2['image2'];
                    $un = $user_name;
                    $up = $user_pic;

                    $posts = new PostModel();
                    $posts->addPost($id_post, $id_user, $title, $content, $favorites, $isDraft,$posted_date,$image1,$image2,$user_name,$user_pic);
                    $listaPosts[] = $posts;
                }

            }
        }
        catch(PDOException $e){
            error_log($e->getMessage());
        }
        finally{
            $statement->closeCursor();
        }

        return $listaPosts;
    }

}



?>