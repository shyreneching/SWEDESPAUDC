CREATE DATABASE  IF NOT EXISTS `musicplayer` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `musicplayer`;
-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: localhost    Database: musicplayer
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `accounts` (
  `username` varchar(30) NOT NULL,
  `password` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `accountType` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `album`
--

DROP TABLE IF EXISTS `album`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `album` (
  `albumid` varchar(15) NOT NULL,
  `albumname` varchar(45) DEFAULT NULL,
  `artist` varchar(15) DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `albumart` longblob,
  PRIMARY KEY (`albumid`),
  KEY `idaccount_idx` (`artist`),
  CONSTRAINT `singer` FOREIGN KEY (`artist`) REFERENCES `accounts` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `album`
--

LOCK TABLES `album` WRITE;
/*!40000 ALTER TABLE `album` DISABLE KEYS */;
/*!40000 ALTER TABLE `album` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `followedplaylist`
--

DROP TABLE IF EXISTS `followedplaylist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `followedplaylist` (
  `idplaylist` varchar(15) NOT NULL,
  `username` varchar(15) NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  `display` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`idplaylist`,`username`),
  KEY `person_idx` (`username`),
  CONSTRAINT `person` FOREIGN KEY (`username`) REFERENCES `accounts` (`username`),
  CONSTRAINT `playlist_1` FOREIGN KEY (`idplaylist`) REFERENCES `playlist` (`idplaylist`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `followedplaylist`
--

LOCK TABLES `followedplaylist` WRITE;
/*!40000 ALTER TABLE `followedplaylist` DISABLE KEYS */;
/*!40000 ALTER TABLE `followedplaylist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `followpeople`
--

DROP TABLE IF EXISTS `followpeople`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `followpeople` (
  `follower` varchar(15) NOT NULL,
  `followed` varchar(15) NOT NULL,
  PRIMARY KEY (`follower`,`followed`),
  KEY `following_idx` (`followed`),
  CONSTRAINT `follower` FOREIGN KEY (`follower`) REFERENCES `accounts` (`username`),
  CONSTRAINT `following` FOREIGN KEY (`followed`) REFERENCES `accounts` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `followpeople`
--

LOCK TABLES `followpeople` WRITE;
/*!40000 ALTER TABLE `followpeople` DISABLE KEYS */;
/*!40000 ALTER TABLE `followpeople` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `playlist`
--

DROP TABLE IF EXISTS `playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `playlist` (
  `idplaylist` varchar(15) NOT NULL,
  `playlistname` varchar(45) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `display` tinyint(4) DEFAULT NULL,
  `datecreated` datetime DEFAULT NULL,
  PRIMARY KEY (`idplaylist`),
  KEY `username_idx` (`username`),
  CONSTRAINT `username` FOREIGN KEY (`username`) REFERENCES `accounts` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist`
--

LOCK TABLES `playlist` WRITE;
/*!40000 ALTER TABLE `playlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recentlyplayed`
--

DROP TABLE IF EXISTS `recentlyplayed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `recentlyplayed` (
  `idrecentlyplayed` int(11) NOT NULL,
  `idsong` varchar(15) NOT NULL,
  `username` varchar(15) NOT NULL,
  `timeplayed` datetime NOT NULL,
  PRIMARY KEY (`idrecentlyplayed`),
  UNIQUE KEY `idrecentlyplayed_UNIQUE` (`idrecentlyplayed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recentlyplayed`
--

LOCK TABLES `recentlyplayed` WRITE;
/*!40000 ALTER TABLE `recentlyplayed` DISABLE KEYS */;
/*!40000 ALTER TABLE `recentlyplayed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `song`
--

DROP TABLE IF EXISTS `song`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `song` (
  `idsong` varchar(15) NOT NULL,
  `songname` varchar(100) DEFAULT NULL,
  `genre` varchar(100) DEFAULT NULL,
  `artist` varchar(100) DEFAULT NULL,
  `albumid` varchar(100) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `trackNumber` int(11) DEFAULT NULL,
  `length` int(11) DEFAULT NULL,
  `size` float DEFAULT NULL,
  `songfile` longblob NOT NULL,
  `dateuploaded` datetime DEFAULT NULL,
  PRIMARY KEY (`idsong`),
  KEY `album_idx` (`albumid`),
  CONSTRAINT `InAlbum` FOREIGN KEY (`albumid`) REFERENCES `album` (`albumid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `song`
--

LOCK TABLES `song` WRITE;
/*!40000 ALTER TABLE `song` DISABLE KEYS */;
/*!40000 ALTER TABLE `song` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `songcollection`
--

DROP TABLE IF EXISTS `songcollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `songcollection` (
  `idplaylist` varchar(15) NOT NULL,
  `idsong` varchar(15) NOT NULL,
  PRIMARY KEY (`idplaylist`,`idsong`),
  KEY `song_idx` (`idsong`),
  KEY `song_id` (`idsong`),
  CONSTRAINT `playlist` FOREIGN KEY (`idplaylist`) REFERENCES `playlist` (`idplaylist`),
  CONSTRAINT `song` FOREIGN KEY (`idsong`) REFERENCES `song` (`idsong`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `songcollection`
--

LOCK TABLES `songcollection` WRITE;
/*!40000 ALTER TABLE `songcollection` DISABLE KEYS */;
/*!40000 ALTER TABLE `songcollection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usersong`
--

DROP TABLE IF EXISTS `usersong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usersong` (
  `idsong` varchar(15) NOT NULL,
  `username` varchar(15) NOT NULL,
  `timesplayed` int(11) DEFAULT NULL,
  PRIMARY KEY (`idsong`,`username`),
  KEY `user_idx` (`username`),
  CONSTRAINT `fk_song` FOREIGN KEY (`idsong`) REFERENCES `song` (`idsong`),
  CONSTRAINT `user` FOREIGN KEY (`username`) REFERENCES `accounts` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usersong`
--

LOCK TABLES `usersong` WRITE;
/*!40000 ALTER TABLE `usersong` DISABLE KEYS */;
/*!40000 ALTER TABLE `usersong` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-04-10 16:06:31
