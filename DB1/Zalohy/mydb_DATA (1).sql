-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Dec 13, 2015 at 04:36 PM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `mydb`
--

--
-- Dumping data for table `pohyb_zbozi`
--

INSERT INTO `pohyb_zbozi` (`id_pohyb`, `prijemka_id_prijemka`, `vydejka_id_vydejka`, `mnozstvi_pouzite`, `cena_na_dokladu`, `skladova_polozka_id_polozka`, `skladova_polozka_sklad_id_sklad`) VALUES
(2, NULL, 1, 1, 4800.02, 9, 1),
(3, NULL, 1, 4, 325.5, 8, 1),
(4, NULL, 1, 10, 90.15, 7, 1),
(5, NULL, 3, 2, 90.15, 7, 1),
(6, NULL, 3, 1, 233.8, 11, 1),
(7, NULL, 3, 1, 1530.01, 12, 2),
(8, NULL, 2, 4, 390.83, 6, 1),
(9, NULL, 2, 18, 90.15, 7, 1),
(10, NULL, 2, 1, 2085.14, 13, 2),
(11, 1, NULL, 16, 302.55, 8, 1),
(12, 1, NULL, 2, 4996.09, 9, 1),
(13, 2, NULL, 2, 233.53, 11, 1),
(14, 2, NULL, 2, 1633.2, 12, 2),
(15, 3, NULL, 28, 400, 6, 1),
(16, 3, NULL, 15, 90, 7, 1),
(17, 3, NULL, 1, 2100, 13, 2),
(18, NULL, 5, 2, 90.15, 6, 1);

--
-- Dumping data for table `prijemka`
--

INSERT INTO `prijemka` (`id_prijemka`, `datum_prijeti`, `dodavatel_nazev`, `dodavatel_kontakt`, `datum_objednani`) VALUES
(1, '2015-11-10', 'OEZ', 'tel.: +420 465 672 111', '2015-11-05'),
(2, NULL, 'OEZ', 'tel.: +420 465 672 111', '2015-12-10'),
(3, '2015-12-01', 'ELDOR', 'tel.: 318 523 066', '2015-11-16');

--
-- Dumping data for table `produkt`
--

INSERT INTO `produkt` (`id_seriove_cislo`, `jmeno`, `zakaznik_id_zakaznik`, `marze_zakazka`, `sleva`, `splatnost`, `parametry`) VALUES
(3, 'Rozvodná skříň', 2, 1.5, 0.05, '2015-12-31', 'Přívod: 125A 3f\r\n4x  25/3\r\n12x 25/1'),
(4, 'Rozvaděč elektroměrový', 1, 1.2, 0.1, '2015-12-31', 'Přívod 25/3\r\n3x  25/3\r\n18x 25/1'),
(5, 'Rovnodná skříň', 1, 1.15, 0.02, '2016-02-09', 'Přívod 25A 3f\r\n5x 25/3');

--
-- Dumping data for table `sklad`
--

INSERT INTO `sklad` (`id_sklad`, `nazev_skladu`, `spravce_jmeno`, `spravce_kontakt`) VALUES
(1, 'Elektro', 'Martin Hamet', 'tel.:21720332074'),
(2, 'Konstrukce', 'Pavel Nekolný', 'e-mail: p.nekolny@centrum.cz');

--
-- Dumping data for table `skladova_polozka`
--

INSERT INTO `skladova_polozka` (`id_polozka`, `nazev`, `typ`, `sklad_id_sklad`, `mnozstvi_type`, `mnozstvi_na_sklade`, `cena`, `marze_polozka`, `vyrobce`) VALUES
(6, 'Ex9BH 3f B 25/3 Jistič 10kA NOARK /100325/', 'Jistič 25/3', 1, 'ks', 8, 390.83, NULL, 'NOARK'),
(7, 'Ex9BH 1f B 25/1 Jistič 10kA NOARK /100280/', 'Jistič 25/1', 1, 'ks', 24, 86.15, NULL, 'NOARK'),
(8, 'LSN 3f B 25/3 Jistič OEZ', 'Jistič 25/3', 1, 'ks', 7, 302.5, NULL, 'OEZ'),
(9, 'BC160NT305-125-L jistič 125A OEZ', 'Jistič 125/3', 1, 'ks', 1, 4996.09, 1.1, 'OEZ'),
(11, 'CS-BC-A011 sada připojovací přední přívod', 'Připojovací sada', 1, 'bal', 2, 233.53, 1.1, 'OEZ'),
(12, 'NP65-0304020', 'Skříň nástěnná', 2, 'ks', 3, 1633.06, 1.05, 'OEZ'),
(13, 'ECO-39P', 'Skříň nástěnná', 2, 'ks', 4, 1968.69, 1.05, 'OEZ');

--
-- Dumping data for table `vydejka`
--

INSERT INTO `vydejka` (`id_vydejka`, `datum_vydani`, `prevzal`, `produkt_id_seriove_cislo`, `produkt_zakaznik_id_zakaznik`) VALUES
(1, '2015-11-27', 'Jaroslav Bouček', 3, 2),
(2, '2015-11-27', 'Václav Černý', 4, 1),
(3, '2015-11-24', 'Vojtěch Vobr', 3, 2),
(5, '2016-01-08', 'Jaroslav Bouček', 3, 2);

--
-- Dumping data for table `zakaznik`
--

INSERT INTO `zakaznik` (`id_zakaznik`, `jmeno`, `prijmeni`, `kontakt`, `adresa_mesto`, `adresa_ulice_cp`, `adresa_psc`) VALUES
(1, 'Libor', 'Bartoloměj', 'tel.: 720856135', 'Dobříš', 'Hostomická 1975', 26301),
(2, 'Jana', 'Vohralíková', 'tel.: 420 224 381 111', 'Praha 6-Suchdol', 'Kamýcká 961/129', 16500);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
