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

-- --------------------------------------------------------

--
-- Stand-in structure for view `material_prijaty`
--
CREATE TABLE IF NOT EXISTS `material_prijaty` (
`nazev` varchar(45)
,`datum_objednani` date
,`datum_prijeti` date
,`dodavatel_nazev` varchar(45)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `material_vydany`
--
CREATE TABLE IF NOT EXISTS `material_vydany` (
`datum_vydani` date
,`nazev` varchar(45)
,`mnozstvi_pouzite` int(10) unsigned
,`prevzal` varchar(45)
);
-- --------------------------------------------------------

--
-- Table structure for table `pohyb_zbozi`
--

CREATE TABLE IF NOT EXISTS `pohyb_zbozi` (
  `id_pohyb` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `prijemka_id_prijemka` int(10) unsigned DEFAULT NULL,
  `vydejka_id_vydejka` int(10) unsigned DEFAULT NULL,
  `mnozstvi_pouzite` int(10) unsigned NOT NULL,
  `cena_na_dokladu` float unsigned NOT NULL,
  `skladova_polozka_id_polozka` int(10) unsigned NOT NULL,
  `skladova_polozka_sklad_id_sklad` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_pohyb`),
  UNIQUE KEY `idpolozka_UNIQUE` (`id_pohyb`),
  KEY `fk_polozka_dokladu_prijemka1_idx` (`prijemka_id_prijemka`),
  KEY `fk_polozka_dokladu_vydejka1_idx` (`vydejka_id_vydejka`),
  KEY `fk_pohyb_zbozi_skladova_polozka1_idx` (`skladova_polozka_id_polozka`,`skladova_polozka_sklad_id_sklad`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=19 ;

-- --------------------------------------------------------

--
-- Table structure for table `prijemka`
--

CREATE TABLE IF NOT EXISTS `prijemka` (
  `id_prijemka` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `datum_prijeti` date DEFAULT NULL,
  `dodavatel_nazev` varchar(45) NOT NULL,
  `dodavatel_kontakt` varchar(45) NOT NULL,
  `datum_objednani` date NOT NULL,
  PRIMARY KEY (`id_prijemka`),
  UNIQUE KEY `idprijemka_UNIQUE` (`id_prijemka`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Table structure for table `produkt`
--

CREATE TABLE IF NOT EXISTS `produkt` (
  `id_seriove_cislo` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `jmeno` varchar(45) NOT NULL,
  `zakaznik_id_zakaznik` int(10) unsigned NOT NULL,
  `marze_zakazka` float unsigned DEFAULT NULL,
  `sleva` float unsigned DEFAULT NULL,
  `splatnost` date NOT NULL,
  `parametry` mediumtext,
  PRIMARY KEY (`id_seriove_cislo`,`zakaznik_id_zakaznik`),
  UNIQUE KEY `idzakazka_UNIQUE` (`id_seriove_cislo`),
  KEY `fk_zakazka_zakaznik1_idx` (`zakaznik_id_zakaznik`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

-- --------------------------------------------------------

--
-- Table structure for table `sklad`
--

CREATE TABLE IF NOT EXISTS `sklad` (
  `id_sklad` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nazev_skladu` varchar(45) NOT NULL,
  `spravce_jmeno` varchar(45) DEFAULT NULL,
  `spravce_kontakt` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_sklad`),
  UNIQUE KEY `id_sklad_UNIQUE` (`id_sklad`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table `skladova_polozka`
--

CREATE TABLE IF NOT EXISTS `skladova_polozka` (
  `id_polozka` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nazev` varchar(45) NOT NULL,
  `typ` varchar(45) DEFAULT NULL,
  `sklad_id_sklad` int(10) unsigned NOT NULL,
  `mnozstvi_type` varchar(5) DEFAULT NULL,
  `mnozstvi_na_sklade` float unsigned DEFAULT NULL,
  `cena` float unsigned DEFAULT NULL,
  `marze_polozka` float unsigned DEFAULT NULL,
  `vyrobce` varchar(45) NOT NULL,
  PRIMARY KEY (`id_polozka`,`sklad_id_sklad`),
  UNIQUE KEY `idpolozka_UNIQUE` (`id_polozka`),
  KEY `fk_polozka_sklad1_idx` (`sklad_id_sklad`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=14 ;

-- --------------------------------------------------------

--
-- Table structure for table `vydejka`
--

CREATE TABLE IF NOT EXISTS `vydejka` (
  `id_vydejka` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `datum_vydani` date NOT NULL,
  `prevzal` varchar(45) NOT NULL,
  `produkt_id_seriove_cislo` int(10) unsigned NOT NULL,
  `produkt_zakaznik_id_zakaznik` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_vydejka`),
  UNIQUE KEY `idvydejka_UNIQUE` (`id_vydejka`),
  KEY `fk_vydejka_produkt1_idx` (`produkt_id_seriove_cislo`,`produkt_zakaznik_id_zakaznik`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

-- --------------------------------------------------------

--
-- Table structure for table `zakaznik`
--

CREATE TABLE IF NOT EXISTS `zakaznik` (
  `id_zakaznik` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `jmeno` varchar(45) NOT NULL,
  `prijmeni` varchar(45) NOT NULL,
  `kontakt` varchar(45) NOT NULL,
  `adresa_mesto` varchar(45) NOT NULL,
  `adresa_ulice_cp` varchar(45) NOT NULL,
  `adresa_psc` int(5) NOT NULL,
  PRIMARY KEY (`id_zakaznik`),
  UNIQUE KEY `idzakaznik_UNIQUE` (`id_zakaznik`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Structure for view `material_prijaty`
--
DROP TABLE IF EXISTS `material_prijaty`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `material_prijaty` AS select `skladova_polozka`.`nazev` AS `nazev`,`prijemka`.`datum_objednani` AS `datum_objednani`,`prijemka`.`datum_prijeti` AS `datum_prijeti`,`prijemka`.`dodavatel_nazev` AS `dodavatel_nazev` from ((`pohyb_zbozi` join `skladova_polozka`) join `prijemka`) where ((`pohyb_zbozi`.`prijemka_id_prijemka` = `prijemka`.`id_prijemka`) and (`pohyb_zbozi`.`skladova_polozka_id_polozka` = `skladova_polozka`.`id_polozka`) and (`pohyb_zbozi`.`prijemka_id_prijemka` > 0));

-- --------------------------------------------------------

--
-- Structure for view `material_vydany`
--
DROP TABLE IF EXISTS `material_vydany`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `material_vydany` AS select `vydejka`.`datum_vydani` AS `datum_vydani`,`skladova_polozka`.`nazev` AS `nazev`,`pohyb_zbozi`.`mnozstvi_pouzite` AS `mnozstvi_pouzite`,`vydejka`.`prevzal` AS `prevzal` from ((`pohyb_zbozi` join `vydejka`) join `skladova_polozka`) where ((`pohyb_zbozi`.`vydejka_id_vydejka` = `vydejka`.`id_vydejka`) and (`pohyb_zbozi`.`vydejka_id_vydejka` > 0) and (`pohyb_zbozi`.`skladova_polozka_id_polozka` = `skladova_polozka`.`id_polozka`)) order by `vydejka`.`datum_vydani`;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pohyb_zbozi`
--
ALTER TABLE `pohyb_zbozi`
  ADD CONSTRAINT `fk_polozka_dokladu_prijemka1` FOREIGN KEY (`prijemka_id_prijemka`) REFERENCES `prijemka` (`id_prijemka`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_polozka_dokladu_vydejka1` FOREIGN KEY (`vydejka_id_vydejka`) REFERENCES `vydejka` (`id_vydejka`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_pohyb_zbozi_skladova_polozka1` FOREIGN KEY (`skladova_polozka_id_polozka`, `skladova_polozka_sklad_id_sklad`) REFERENCES `skladova_polozka` (`id_polozka`, `sklad_id_sklad`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `produkt`
--
ALTER TABLE `produkt`
  ADD CONSTRAINT `fk_zakazka_zakaznik1` FOREIGN KEY (`zakaznik_id_zakaznik`) REFERENCES `zakaznik` (`id_zakaznik`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `skladova_polozka`
--
ALTER TABLE `skladova_polozka`
  ADD CONSTRAINT `fk_polozka_sklad1` FOREIGN KEY (`sklad_id_sklad`) REFERENCES `sklad` (`id_sklad`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `vydejka`
--
ALTER TABLE `vydejka`
  ADD CONSTRAINT `fk_vydejka_produkt1` FOREIGN KEY (`produkt_id_seriove_cislo`, `produkt_zakaznik_id_zakaznik`) REFERENCES `produkt` (`id_seriove_cislo`, `zakaznik_id_zakaznik`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
