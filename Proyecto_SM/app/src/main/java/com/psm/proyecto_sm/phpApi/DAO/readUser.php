<?php
include("config.php");
$id_user = $_POST['id_user'];

try{
    $sql = 'SELECT * FROM USERS WHERE id_user = ?';

    $statement = $this->connection->prepare($sql);
    $statement->bindParam(1,$id_user);
    $statement->execute();

    while ($row = $statement->fetch(PDO::FETCH_ASSOC)){
        
        $Id_Curso = $row['Id_Curso'];
        $Titulo = $row['Titulo'];
        $Descripcion = $row['Descripcion'];
        $Fecha_Creacion = $row['Fecha_Creacion'];
        $Cant_Niveles = $row['Cant_Niveles'];
        $Costo = $row['Costo'];
        $Imagen = $row['Imagen'];
        $Promedio = $row['Promedio'];
        $Vacio = $row['Vacio'];
        $Activo = $row['Activo'];
        $Id_Usuario = $row['Id_Usuario'];
        $Nombre_Usuario = $row['Nombre_Usuario'];
        $Foto_Usuario = $row['Foto_Usuario'];
        $Ingreso_Curso = $row['Ingreso_Curso'];

        $curso = new CursoModel();
        $curso->addCurso($Id_Curso, $Titulo, $Descripcion, $Fecha_Creacion, $Cant_Niveles, $Costo, $Imagen, $Promedio, $Vacio, $Activo, $Id_Usuario, $Nombre_Usuario, $Foto_Usuario, $Ingreso_Curso);
        $listaCurso[] = $curso;

    }
}
catch(PDOException $e){
    error_log($e->getMessage());
}
finally{
    $statement->closeCursor();
}
?>