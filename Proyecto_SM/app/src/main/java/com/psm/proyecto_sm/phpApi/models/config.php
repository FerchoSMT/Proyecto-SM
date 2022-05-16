<?php

class BaseDeDatos
{
    private $host = "localhost";
    private $dbName = "u625118055_bd_buzztalk";
    private $username = "u625118055_root";
    private $password = "dHLKi9Vy";

    private $con;

    public function connect()
    {
        $this->con = null;

        $dsn = 'mysql:host=' . $this->host . ';dbname=' . $this->dbName;

        $this->con = new PDO($dsn, $this->username, $this->password);

        $this->con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $this->con->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);

        return $this->con;
    }
}

?>