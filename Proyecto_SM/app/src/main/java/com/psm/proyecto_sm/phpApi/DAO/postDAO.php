<?php

require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/userModel.php';
require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/postModel.php';
require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/replyModel.php';
require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/config.php';
require once $_SERVER['DOCUMENT_ROOT'] . './phpApi/DAO/userDAO.php';

class UsuarioDAO{

    private $connection;

    public function __construct(){
        $database = new BaseDeDatos;
        $this->connection = $database->connect();
    }

    public function __destruct(){
        $this->connection = null;
    }

    $userDAO = new UsuarioDAO();

    readPost($ps){
        $listaPosts = [];
        
        try{
            $sql = 'SELECT * FROM POSTS WHERE id_post = ?';

            $statement = $this->connection->prepare($sql);
            $statement->bindParam(1,$ps);
            $statement->execute();

            while ($row = $statement->fetch(PDO::FETCH_ASSOC)){
                
                $id_post = $row['id_post'];
                $title = $row['title'];
                $content = $row['content'];
                $content = $row['content'];
                $favorites = $row['content'];
                $isDraft = $row['content'];
                $posted_date = $row['content'];
                $image1 = $row['content'];
                $image2 = $row['content'];

                $un = "";
                $pfp = null;
                

                $sql2 = 'SELECT * FROM USERS WHERE id_user = ?';
                $statement2 = $this->connection->prepare($sql2);
                $statement2->bindParam(1,$ps);
                $statement2->execute();
                while ($row2 = $statement2->fetch(PDO::FETCH_ASSOC)){
                    $un = $row['name'];
                    $pfp = $row['profilePic'];
                }

                $post = new PostModel();
                $post->addPost($id_post, $id_user, $title, $content, $favorites, $isDraft,$posted_date,$image1,$image2,$un,$pfp);
                $listaPosts[] = $post;

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

    readAllPosts(){
        $listaPosts = [];
        
        try{
            $sql = 'SELECT * FROM POSTS';

            $statement = $this->connection->prepare($sql);
            $statement->execute();

            while ($row = $statement->fetch(PDO::FETCH_ASSOC)){
                
                $id_post = $row['id_post'];
                $title = $row['title'];
                $content = $row['content'];
                $content = $row['content'];
                $favorites = $row['content'];
                $isDraft = $row['content'];
                $posted_date = $row['content'];
                $image1 = $row['content'];
                $image2 = $row['content'];

                $un = "";
                $pfp = null;
                

                $sql2 = 'SELECT * FROM USERS WHERE id_user = ?';
                $statement2 = $this->connection->prepare($sql2);
                $statement2->bindParam(1,$ps);
                $statement2->execute();
                while ($row2 = $statement2->fetch(PDO::FETCH_ASSOC)){
                    $un = $row['name'];
                    $pfp = $row['profilePic'];
                }

                $post = new PostModel();
                $post->addPost($id_post, $id_user, $title, $content, $favorites, $isDraft,$posted_date,$image1,$image2,$un,$pfp);
                $listaPosts[] = $post;

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

    readPostReplies($ps){
        $listaReplies = [];
        
        
        try{
            $sql = 'SELECT * FROM REPLIES WHERE id_post = ? ORDER BY Replied_Date ASC';

            $statement = $this->connection->prepare($sql);
            $statement->bindParam(1,$ps);
            $statement->execute();

            while ($row = $statement->fetch(PDO::FETCH_ASSOC)){
                
                $id_post = $row['id_post'];
                $id_reply = $row['id_reply'];
                $content = $row['content'];
                $id_user = $row['id_user'];
                $un = "";
                $pfp = null;
                

                $sql2 = 'SELECT * FROM USERS WHERE id_user = ?';
                $statement2 = $this->connection->prepare($sql2);
                $statement2->bindParam(1,$ps);
                $statement2->execute();
                while ($row2 = $statement2->fetch(PDO::FETCH_ASSOC)){
                    $un = $row['name'];
                    $pfp = $row['profilePic'];
                }

                $reply = new ReplyModel();
                $reply->addReply($id_post, $id_user, $id_reply, $content, $replied_date, $un,$pfp);
                $listaReplies[] = $reply;

            }
        }
        catch(PDOException $e){
            error_log($e->getMessage());
        }
        finally{
            $statement->closeCursor();
        }

        return $listaReplies;
    }

    searchPosts($sr){
        $listaPosts = [];
        
        try{
            $sql = 'SELECT * FROM POSTS WHERE isdraft = 0 AND title = %?% or content = %?% ORDER BY Posted_Date DESC';

            $statement = $this->connection->prepare($sql);
            $statement->bindParam(1,$sr);
            $statement->bindParam(2,$sr);
            $statement->execute();

            while ($row = $statement->fetch(PDO::FETCH_ASSOC)){
                
                $id_post = $row['id_post'];
                $title = $row['title'];
                $content = $row['content'];
                $content = $row['content'];
                $favorites = $row['content'];
                $isDraft = $row['content'];
                $posted_date = $row['content'];
                $image1 = $row['content'];
                $image2 = $row['content'];

                $un = "";
                $pfp = null;
                

                $sql2 = 'SELECT * FROM USERS WHERE id_user = ?';
                $statement2 = $this->connection->prepare($sql2);
                $statement2->bindParam(1,$ps);
                $statement2->execute();
                while ($row2 = $statement2->fetch(PDO::FETCH_ASSOC)){
                    $un = $row['name'];
                    $pfp = $row['profilePic'];
                }

                $post = new PostModel();
                $post->addPost($id_post, $id_user, $title, $content, $favorites, $isDraft,$posted_date,$image1,$image2,$un,$pfp);
                $listaPosts[] = $post;

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