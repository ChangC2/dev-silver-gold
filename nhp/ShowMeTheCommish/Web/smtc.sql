-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 20, 2024 at 08:54 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `smtc`
--

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE `comment` (
  `id` int(11) NOT NULL,
  `target` int(11) NOT NULL COMMENT 'type = 0 => property id, type = 1 => comment id',
  `user` int(11) NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '0: Comment, 1: Reply',
  `is_read` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`id`, `target`, `user`, `user_name`, `content`, `created_at`, `type`, `is_read`) VALUES
(598, 18, 18, 'Mone V', 'How is going the project? All done?', '2024-06-07 22:22:51', 0, 0),
(599, 18, 18, 'Mone V', 'That is great. Please send me test link.', '2024-06-07 22:24:02', 0, 0),
(600, 2, 15, 'Jay Mirando', 'Hello, how are you?', '2024-06-19 19:26:33', 0, 0),
(601, 2, 15, 'Jay Mirando', 'Thank you', '2024-06-19 19:27:06', 0, 0),
(602, 2, 13, 'Carol Tomayko', 'Good morning', '2024-06-21 18:42:42', 0, 0),
(603, 2, 15, 'Jay Mirando', 'That is good', '2024-06-21 19:16:37', 0, 0),
(604, 2, 15, 'Jay Mirando', 'Ok', '2024-06-21 19:20:20', 0, 0),
(605, 2, 15, 'Jay Mirando', 'Good', '2024-06-21 19:21:03', 0, 0),
(606, 2, 15, 'Jay Mirando', 'Hello, how are you?', '2024-06-21 19:21:09', 0, 0),
(607, 600, 13, 'Carol Tomayko', 'Thank you', '2024-06-22 22:51:52', 1, 0),
(608, 600, 13, 'Carol Tomayko', 'You are welcome', '2024-06-22 22:52:11', 1, 0),
(609, 600, 13, 'Carol Tomayko', 'That is great', '2024-06-22 23:14:08', 1, 0),
(610, 600, 13, 'Carol Tomayko', 'Good', '2024-06-22 23:22:54', 1, 0),
(611, 600, 13, 'Carol Tomayko', 'Good', '2024-06-22 23:22:56', 1, 0),
(612, 600, 13, 'Carol Tomayko', 'Good', '2024-06-22 23:23:00', 1, 0),
(613, 600, 13, 'Carol Tomayko', 'Good', '2024-06-22 23:23:04', 1, 0),
(614, 600, 13, 'Carol Tomayko', 'Good', '2024-06-22 23:23:09', 1, 0),
(615, 600, 13, 'Carol Tomayko', 'Good', '2024-06-22 23:23:16', 1, 0),
(616, 7, 13, 'Carol Tomayko', 'Hello, how are you?', '2024-06-24 12:58:35', 0, 0),
(617, 1, 15, 'Jay Mirando', 'Hello, I hope to buy this property.', '2024-06-26 18:28:45', 0, 0),
(618, 1, 15, 'Jay Mirando', 'Hello, I hope to buy this property.', '2024-06-26 18:28:56', 0, 0),
(619, 1, 15, 'Jay Mirando', 'Great Property', '2024-06-26 18:29:52', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `plans`
--

CREATE TABLE `plans` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` float(10,2) NOT NULL DEFAULT 0.00 COMMENT 'Minimum amount is $0.50 US',
  `interval` enum('day','week','month','year') NOT NULL DEFAULT 'month',
  `interval_count` tinyint(2) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `plans`
--

INSERT INTO `plans` (`id`, `name`, `price`, `interval`, `interval_count`) VALUES
(1, 'Daily Subscription', 15.00, 'day', 1),
(2, 'Weekly Subscription', 25.00, 'week', 1),
(3, 'Monthly Subscription', 5.00, 'month', 1),
(4, 'Quarterly Subscription', 199.00, 'month', 3),
(5, 'Half-yearly Subscription', 255.00, 'month', 6),
(6, 'Yearly Subscription', 349.00, 'year', 1);

-- --------------------------------------------------------

--
-- Table structure for table `property`
--

CREATE TABLE `property` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `commission_type` int(11) NOT NULL DEFAULT 0 COMMENT '0 : Fixed Price, 1 : Percentage',
  `commission_value` double NOT NULL DEFAULT 0,
  `description` longtext CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `address` varchar(255) NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `image` varchar(255) NOT NULL DEFAULT '',
  `user` int(11) NOT NULL DEFAULT 0 COMMENT '0 : Fixed Price, 1 : Percentage',
  `active` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `property`
--

INSERT INTO `property` (`id`, `name`, `commission_type`, `commission_value`, `description`, `address`, `created_at`, `image`, `user`, `active`) VALUES
(1, 'Charming all-brick 4-bedroom home', 0, 325, 'Introducing a charming all-brick 4-bedroom home, situated at the end of a private drive. This stunning property features a one-acre park-like lot w/a semi-fenced yar, offering privacy & tranquility. Inside you\'ll find a spacious family room w/a stone fireplace, perfect for cozy gatherings. The kitchen boasts granite countertops & stainless appliances. The master bedroom comes w/an en-suite bathroom, providing a private retreat. Additionally, there is a convenient first floor laundry room. Updates to the property include newer windows & doors, upgraded flooring, & updated breaker box. The furnace & hot water heater were replaced in 2022 & 2021 respectively. With a 3-car garage & oversize asphalt driveway, parking is a breeze. Overflow space for all of your toys in the 8x12 shed. Located just around the corner from Mcquistion Elementary School, this home is ideal for families. Don\'t miss the opportunity to own this exceptional property with shopping & restaurants a short drive away!', '110 Hillvue Ln, Butler, PA 16001', '2024-06-02 14:43:55', 'http://localhost/smtc/images/property/20240602075243_4.jpg', 13, 1),
(2, 'Beautiful 3 bed 3 bath remodeled home with a pool', 0, 455, 'Beautiful 3 bed 3 bath remodeled home with a pool! This home boasts brand new shaker cabinets with calacatta quartz countertops, brand new luxury vinyl plank, new carpet, newly tiled bathrooms, new paint, with a big pool in the back yard to cool off in the vegas summer. A shaded patio so that you can set up a table and enjoy the outdoors! And also, a HUGE parking area in the front of the home. Also located conveniently to the summerlin pkwy and to Tivoli Village! Come get this home soon as it will not last long!!!', '6413 Titan Ct, Las Vegas, NV 89108', '2024-06-02 14:43:55', 'http://localhost/smtc/images/property/20240602075243_4.jpg', 13, 1),
(3, 'Beautifully remodeled Cape-style home', 0, 305000, 'Welcome to this beautifully remodeled Cape-style home, originally built in 1954. This inviting property seamlessly blends classic charm with modern amenities, featuring a contemporary kitchen with stunning granite countertops and stainless steel appliances. A convenient slider opens from the mud room to the backyard, perfect for outdoor entertaining, with a fenced yard and a versatile workshop shed. The dining area boasts gleaming hardwood floors, seamlessly connecting to the living room, which is highlighted by a cozy wood-burning stove. The main floor accommodates two spacious bedrooms and a fully remodeled bathroom, ensuring comfort and convenience. Upstairs, you\'ll find two additional bedrooms, offering flexibility for family, guests, or a home office. The lower level features a finished family room, ideal for recreation or relaxation, and a well-equipped laundry room complete with a washer, dryer, and sink. This vinyl sided home is topped with a roof that\'s only 8 years old, ensuring durability and peace of mind. Don\'t miss the opportunity to own this delightful Cape-style home that perfectly combines vintage appeal with modern upgrades. With its array of features and thoughtful remodels, this property is ready to welcome you home. Seller will be accepting Highest & Best offers by 3p 6/9/24', '34 Dale Rd, Enfield, CT 06082', '2024-06-02 14:43:55', 'http://localhost/smtc/images/property/20240602075243_4.jpg', 18, 1),
(4, 'Charming 3-bedroom brick ranch', 0, 230000, 'Welcome to your dream home in the heart of Warren! This charming 3-bedroom brick ranch, located in the sought-after Warren Consolidated School District, offers a perfect blend of comfort and modern updates. Step inside to discover beautifully refinished hardwood floors and freshly painted walls in soothing tones that create a warm and inviting atmosphere. The updated kitchen is a chef\'s delight, featuring newer stainless steel appliances that combine style and functionality. Adjacent to the kitchen, the dining room boasts a new glass sliding door that opens to an enclosed back patio, perfect for enjoying your morning coffee or evening relaxation. Stay cool during the summer months with the newly installed AC unit, and enjoy peace of mind with the new hot water tank. The finished basement provides additional living space and ample storage, making it ideal for entertaining guests or creating a cozy family retreat. Ductwork was just cleaned last year providing the new owner with a breathe of fresh air. Conveniently located near major expressways, this home offers easy access for commuters. Don\'t miss the opportunity to make this beautifully updated ranch your new home!', '26732 Patricia Ave, Warren, MI 48091', '2024-06-02 14:43:55', 'http://localhost/smtc/images/property/20240602075243_4.jpg', 18, 1),
(5, 'Beautifully upgraded single-story home', 0, 424995, 'Welcome to this beautifully upgraded single-story home featuring 3 bedrooms and 2 baths. The large primary suite boasts a luxurious bathroom with a shower, Roman tub, and double vanity, offering a serene retreat. The spacious layout includes a 2-car garage, a cozy bar area, and a charming tile fireplace, perfect for entertaining. The home is adorned with windows fitted with stylish shutters, adding both privacy and elegance. This property combines modern comforts with classic touches, making it the ideal place to call home. Don\'t miss out on this gem!', '451 Leighann Rd, Henderson, NV 89015', '2024-06-02 14:43:55', 'http://localhost/smtc/images/property/20240602075243_4.jpg', 19, 1),
(6, 'Charming 3 bedroom 2 bathroom home', 0, 390000, 'Welcome to this charming 3 bedroom 2 bathroom home in the heart of Old Town Windsor. This property is close to everything Windsor has to offer! Walking distance to Skyview Elementary School, parks, restaurants, groceries, & shopping. This residence features fully manicured front & back yards, creating an oasis & picturesque setting perfect for outdoor enjoyment & entertaining. The artistic wood fire pit is the focal point of the backyards sizable patio & gardening area. Inside, you\'ll find a warm & inviting atmosphere, w/ spaces designed for comfortable living. A gas fire place offers warmth on those cooler days. Other notable features are the detached oversize 1 car garage & outdoor gazebo. No HOA or Metro District! The concrete drive offers opportunity for RV/boat parking. Experience the best of Windsor living in this delightful home, offering both a tranquil retreat & a convenient location. Don\'t miss your chance to make this exceptional property your own & enjoy all that Old Town Windsor has to offer.', '590 Pine Dr, Windsor, CO 80550', '2024-06-02 14:43:55', 'http://localhost/smtc/images/property/20240602075243_4.jpg', 19, 1),
(7, 'Hurry To See This Immaculate', 0, 229900, '** MULTIPLE OFFERS RECEIVED** Hurry To See This Immaculate & Updated 3 Bedroom, 3 Full Bath Ranch with Finished Basement in Mentor-on the-Lake! You\'ll Love The Beautiful Refinished Hardwood Floors In The Living Room, Dining Room & All 3 Bedrooms. The Master Bedroom Features A Stunning, Full en-suite Bathroom with A Gorgeous Shower! Off The Dining Room Is A Slider To The 21 x 10 Covered Back Porch Which Overlooks The Fully Fenced Backyard. All Bathrooms Have Been Beautifully Remodeled (Lower Level Full Bath Was Completed in May 2024). The Lower Level is Spectacular with LVT Flooring Throughout A Large Recreation Room, A Possible 4th Bedroom, Brand New (2024) Full Kitchen & A Full Bath! The Laundry/Utility Room Comes Complete with WiFi Enabled Washer & Dryer (2 Yrs Old). The Tech Continues with Smart Light switches in Hallway, Dining Room & Back Door, & Some Phillips Hue Bulbs. The Ring Doorbell & TV Mounts Stay. Other Notable Updates Include: Roof Tear Off, Gutters, Downspouts & Soffits (2023) Windows (excl. Bay in LR 2019). This Fantastic Home Is Just A Short Distance From Lake Erie & Overlook Beach Park! A 100% Money Back Guarantee Is Available For Additional Peace of Mind! Call Today For Your Private Showing.', '7531 Goldenrod Dr, Mentor On The Lake, OH 44060', '2024-06-02 14:43:55', 'http://localhost/smtc/images/property/20240602075243_4.jpg', 20, 1),
(8, 'Nicely updated three bedroom, three full bath ranch home in great St. Peters location', 0, 325000, 'Nicely updated three bedroom, three full bath ranch home in great St. Peters location. Newer flooring throughout the main level. Open floor plan with vaulted ceiling and cozy wood burning fireplace in the Great Room. Lots of cabinets in the Kitchen (Fridge to remain). The Breakfast Room offers an atrium door opening to the spacious, fenced yard with fantastic large patio. Primary Suite features a vaulted ceiling, walk-in closet and ensuite bath with tub and separate shower. The Finished Lower Level offers a huge rec room, potential fourth bedroom/sleeping room (currently used as office space and guest bedroom) and a full bath and still plenty of storage space. Fenced level yard with swingset to remain.', '126 Green Forest Estates Dr, Saint Peters, MO 63376', '2024-06-02 14:43:55', 'http://localhost/smtc/images/property/20240602075243_4.jpg', 20, 1),
(9, 'Newly renovated 3 bedroom, 2 bathroom ranch-style home', 0, 275000, 'Discover the allure of this newly renovated 3 bedroom, 2 bathroom ranch-style home. No detail has been overlooked, as every finish has been meticulously attended to. The home showcases a stunning kitchen adorned with tile backsplash, exquisite quartz countertops, a stylish hood vent, and sleek stainless steel appliances, ensuring the perfect culinary experience. In addition, both bathrooms have been tastefully updated to exemplify modern elegance.\r\n\r\nThis residence also features an above ground pool for leisure and entertainment, as well as a 10x20 workshop with power and an AC window unit, providing a comfortable space for hobbies or projects alike. Additionally, the property offers a 30 AMP RV power hookup, adaptable for charging an electric vehicle, showcasing the forward-thinking elements of this home. Situated in a prime location known for its exceptional schools and everyday conveniences, residents enjoy easy access to shopping, dining, hospitals, and major roadways, including the new toll road 23 and less that 30-minutes to NAS JAX and Cecil Field.\r\n\r\nDo not miss the opportunity to make this exquisite property your own. Schedule a viewing today and begin your journey toward homeownership bliss!', '2709 TINA Lane, Middleburg, FL 32068', '2024-06-02 14:43:55', 'http://localhost/smtc/images/property/20240602075243_4.jpg', 13, 1),
(10, 'Nicely updated three bedroom, three full bath ranch home', 0, 325000, 'Nicely updated three bedroom, three full bath ranch home in great St. Peters location. Newer flooring throughout the main level. Open floor plan with vaulted ceiling and cozy wood burning fireplace in the Great Room. Lots of cabinets in the Kitchen (Fridge to remain). The Breakfast Room offers an atrium door opening to the spacious, fenced yard with fantastic large patio. Primary Suite features a vaulted ceiling, walk-in closet and ensuite bath with tub and separate shower. The Finished Lower Level offers a huge rec room, potential fourth bedroom/sleeping room (currently used as office space and guest bedroom) and a full bath and still plenty of storage space. Fenced level yard with swingset to remain.', '126 Green Forest Estates Dr, Saint Peters, MO 63376', '2024-06-02 14:43:55', 'http://localhost/smtc/images/property/20240602075243_4.jpg', 13, 1),
(11, 'Featuring 5 bedrooms and 2 full baths', 0, 224500, 'Welcome to 1106 Dickinson, a fully renovated 1925 home located on the SE side of Grand Rapids, MI. Featuring 5 bedrooms and 2 full baths, this property combines historic charm with modern updates. Inside, you\'ll discover new flooring, a brand-new kitchen with new appliances, and freshly installed windows. The basement has been fully finished to provide additional living space. Outside, the large backyard and extra off-street parking complement the convenience of a 1-stall garage. With thoughtful upgrades throughout, this home is perfect for anyone seeking a move-in ready property in a vibrant neighborhood. Offer deadline is to be on Monday, June 10th at 2pm.', '1106 Dickinson St SE, Grand Rapids, MI 49507', '2024-06-02 14:43:55', 'http://localhost/smtc/images/property/20240602075243_4.jpg', 18, 1),
(12, 'Gorgeous rancher nestled on 1 acre that\'s surrounded by picturesque views', 0, 399900, 'Gorgeous rancher nestled on 1 acre that\'s surrounded by picturesque views! This home is ready for your personal touches and offers 3 bedrooms, 2 full bathrooms, a spacious living room, a kitchen with loads of cabinet space that flows to the dining area, a full basement with laundry, is roughed-in for a bathroom, and offers walk-out access to the backyard. You\'ll love spending time and entertaining on the oversized deck that overlooks your dominion. Don’t wait too long or this opportunity might pass you by.', '8002 Bennett Branch Rd, Mount Airy, MD 21771', '2024-06-02 14:43:55', 'http://localhost/smtc/images/property/20240602075243_4.jpg', 18, 1);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` int(11) NOT NULL DEFAULT 2 COMMENT '0: Buying Agent\r\n1: Selling Agent\r\n2: Adminisrator\r\n',
  `active` int(11) NOT NULL DEFAULT 0,
  `first_name` varchar(255) NOT NULL DEFAULT '',
  `last_name` varchar(255) NOT NULL DEFAULT '',
  `verification_code` varchar(255) NOT NULL DEFAULT '',
  `bio` longtext CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `mobile_number` varchar(255) NOT NULL DEFAULT '',
  `address` varchar(255) NOT NULL DEFAULT '',
  `city` varchar(255) NOT NULL DEFAULT '',
  `street` varchar(255) NOT NULL DEFAULT '',
  `state` varchar(255) NOT NULL DEFAULT '',
  `zip` varchar(255) NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `name`, `email`, `password`, `role`, `active`, `first_name`, `last_name`, `verification_code`, `bio`, `mobile_number`, `address`, `city`, `street`, `state`, `zip`, `created_at`) VALUES
(2, 'Xavier De la Piedra', 'admin@gmail.com', '$2y$10$YgehQ1adVQi2fFCwDlMKFeJgbGaaIXjbWia7Ti7QI3924KKGB6REa', 2, 1, 'Xavier', 'De la Piedra', '', NULL, '941-300-4166', '', '', '', '', '', '2024-05-29 15:35:29'),
(13, 'Carol Tomayko', 'customer1@gmail.com', '$2y$10$YycRfNyCR6FenhBFlfRa6.O8bbrCZ8v5aEsfG6htKdStoSZBA4nzu', 0, 1, 'Carol', 'Tomayko', '', 'Passion for Real Estate:  Carol’s love for the real estate industry is evident in her dedication to her clients and her work.  She genuinely enjoys helping buyers and sellers achieve their real estate goals and is committed to making the process as smooth and stress-free as possible.\r\nOn a personal note, Carol has three married children and two absolutely amazing grandchildren that she loves spending time with. Carol also enjoys helping others whether it is family, her local church, a friend or her community.\r\n\r\nOverall, Carol Tomayko’s experience, success, commitment to client satisfaction, work ethic communication skills, trustworthiness and passion for real estate make her an ideal choice for anyone seeking a reliable and exceptional real estate professional.', '941-300-4166', '110 Hillvue Ln, Butler, PA 16001', 'Phoenix', 'Cambridge', 'Arizona', '85016', '2024-05-29 15:35:29'),
(15, 'Jay Mirando', 'customer2@gmail.com', '$2y$10$TwKkNdLXAdNrE/F3cj2DNOoUcmIzZKfuhno68Uq/p/y090h3CK2Ne', 0, 1, 'Jay', 'Mirando', '9380', 'Las Vegas Realtor with over 9 years of experience who specializes in Investments, Probates, Trust sales, Foreclosures, Creative Finance, and Renovated properties. Pays attention to detail and focuses on the client so your needs are met!', '941-300-4166', '6413 Titan Ct, Las Vegas, NV 89108', '', '', '', '', '2024-06-01 04:11:12'),
(18, 'Stacy Weingarten', 'customer3@gmail.com', '$2y$10$8NwR3eOT70n4QQpnv6r3XuhfOLs3iiXZmas8KaynBDTOrgzVBZE9a', 1, 1, 'Stacy', 'Weingarten', '', 'As an agent with a team who are experts in this local area, we bring a wealth of knowledge and expertise about buying and selling real estate. It\'s not the same everywhere, so you need a team you can trust for up-to-date information. With over 100 homes s', '941-300-4166', '34 Dale Rd, Enfield, CT 06082', 'Vancouver', 'Vancouver', 'Vancouver', 'Vancouver', '2024-06-07 16:37:19'),
(19, 'Candis Ticconi', 'customer4@gmail.com', '$2y$10$8NwR3eOT70n4QQpnv6r3XuhfOLs3iiXZmas8KaynBDTOrgzVBZE9a', 1, 1, 'Candis', 'Ticconi', '', 'I\'m a local Realtor servicing the Oakland and Macomb county areas. After being in Real Estate for almost 8 years I\'ve recognized and value the trust my clients place in me and I strive everyday to exceed their expectations. I pride myself with building a permanate friendship with my clients. I aim to ensure that the home buying and selling process is fun, easy and stress-free. My clients can count on me any time of the day when they have a question or concern.', '941-300-4166', '26732 Patricia Ave, Warren, MI 48091', 'Vancouver', 'Vancouver', 'Vancouver', 'Vancouver', '2024-06-07 16:37:19'),
(20, 'Kevin Goujon', 'mzhou9954@gmail.com', '$2y$10$8NwR3eOT70n4QQpnv6r3XuhfOLs3iiXZmas8KaynBDTOrgzVBZE9a', 1, 1, 'Kevin', 'Goujon', '', 'Meet Kevin Goujon, a skilled Las Vegas realtor with 15 years in the industry. Specializing in, listings, investors, and buyers, he expertly navigates the Las Vegas, Henderson, and North Las Vegas markets. Whether it’s sales or property management, Kevin’s client-focused approach and local expertise make him an ideal choice for all your real estate needs.', '941-300-4166', '451 Leighann Rd, Henderson, NV 89015', 'Vancouver', 'Vancouver', 'Vancouver', 'Vancouver', '2024-06-07 16:37:19');

-- --------------------------------------------------------

--
-- Table structure for table `user_subscriptions`
--

CREATE TABLE `user_subscriptions` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT 0 COMMENT 'foreign key of "users" table',
  `plan_id` int(5) DEFAULT NULL COMMENT 'foreign key of "plans" table',
  `stripe_customer_id` varchar(50) NOT NULL,
  `stripe_plan_price_id` varchar(255) DEFAULT NULL,
  `stripe_payment_intent_id` varchar(50) NOT NULL,
  `stripe_subscription_id` varchar(50) NOT NULL,
  `default_payment_method` varchar(255) DEFAULT NULL,
  `default_source` varchar(255) DEFAULT NULL,
  `paid_amount` float(10,2) NOT NULL,
  `paid_amount_currency` varchar(10) NOT NULL,
  `plan_interval` varchar(10) NOT NULL,
  `plan_interval_count` tinyint(2) NOT NULL DEFAULT 1,
  `customer_name` varchar(50) DEFAULT NULL,
  `customer_email` varchar(50) DEFAULT NULL,
  `plan_period_start` datetime DEFAULT NULL,
  `plan_period_end` datetime DEFAULT NULL,
  `created` datetime NOT NULL,
  `status` varchar(50) NOT NULL,
  `street` varchar(255) DEFAULT '',
  `city` varchar(255) DEFAULT '',
  `zip` varchar(255) DEFAULT '',
  `card_name` varchar(255) DEFAULT '',
  `address_cc` varchar(255) DEFAULT '',
  `state` varchar(255) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `user_subscriptions`
--

INSERT INTO `user_subscriptions` (`id`, `user_id`, `plan_id`, `stripe_customer_id`, `stripe_plan_price_id`, `stripe_payment_intent_id`, `stripe_subscription_id`, `default_payment_method`, `default_source`, `paid_amount`, `paid_amount_currency`, `plan_interval`, `plan_interval_count`, `customer_name`, `customer_email`, `plan_period_start`, `plan_period_end`, `created`, `status`, `street`, `city`, `zip`, `card_name`, `address_cc`, `state`) VALUES
(1, 14, 1, 'cus_QEvjkf45mVnd4q', 'price_1PORsGLuEqhyf17nUVVnZbz3', 'pi_3PORsHLuEqhyf17n0WANEZb9', 'sub_1PORsHLuEqhyf17nAA5y456L', 'pm_1PORsILuEqhyf17nbxX1tZSe', NULL, 5.00, 'usd', 'month', 1, 'Female', 'gametest826@gmail.com', '2024-06-05 22:00:05', '2024-07-05 22:00:05', '2024-06-05 22:00:05', 'succeeded', '', '', '', '', '', ''),
(2, 14, 1, 'cus_QEvl1VSEvn9lkX', 'price_1PORtbLuEqhyf17nmAcoZCvS', 'pi_3PORtcLuEqhyf17n0tGb6gyL', 'sub_1PORtcLuEqhyf17ne5yTameV', 'pm_1PORtdLuEqhyf17nTIyffQN4', NULL, 5.00, 'usd', 'month', 1, 'Female', 'gametest826@gmail.com', '2024-06-05 22:01:28', '2024-07-05 22:01:28', '2024-06-05 22:01:28', 'succeeded', '', '', '', '', '', ''),
(3, 14, 1, 'cus_QEvxYwH3sJIfzW', 'price_1POS5PLuEqhyf17nBcMexVHa', 'pi_3POS5QLuEqhyf17n1KLNrWV3', 'sub_1POS5PLuEqhyf17nkMuh4cDX', 'pm_1POS5RLuEqhyf17nVQw5byUG', NULL, 5.00, 'usd', 'month', 1, 'Sample Voice 1', 'gametest826@gmail.com', '2024-06-05 22:13:39', '2024-07-05 22:13:39', '2024-06-05 22:13:39', 'succeeded', '', '', '', '', '', ''),
(5, 18, 1, 'cus_QFUEI4nUU8WvSU', 'price_1POzFyLuEqhyf17nHjlAQHJ9', 'pi_3POzFzLuEqhyf17n0y7gVgQ2', 'sub_1POzFzLuEqhyf17n8YcTGwNI', 'pm_1POzG0LuEqhyf17nSIABbcEG', NULL, 5.00, 'usd', 'month', 1, 'Mone V', 'gametest826@gmail.com', '2024-06-07 09:38:47', '2024-07-07 09:38:47', '2024-06-07 09:38:47', 'succeeded', '', '', '', '', '', ''),
(6, 18, 1, 'cus_QFUKCKfVZZiYml', 'price_1POzMCLuEqhyf17nFZ3HZkNR', 'pi_3POzMDLuEqhyf17n08so9j60', 'sub_1POzMDLuEqhyf17nKs2n6lVS', 'pm_1POzMELuEqhyf17n1BfzwLR5', NULL, 5.00, 'usd', 'month', 1, 'Mone V', 'gametest826@gmail.com', '2024-06-07 09:45:13', '2024-07-07 09:45:13', '2024-06-07 09:45:13', 'succeeded', '', '', '', '', '', ''),
(7, 18, 1, 'cus_QFULVSbzUWftQj', 'price_1POzMuLuEqhyf17nm4no81nW', 'pi_3POzMvLuEqhyf17n0esLO1U5', 'sub_1POzMvLuEqhyf17nb4mb9hZ6', 'pm_1POzMwLuEqhyf17nKRO3DT1Z', NULL, 5.00, 'usd', 'month', 1, 'Mone V', 'gametest826@gmail.com', '2024-06-07 09:45:57', '2024-07-07 09:45:57', '2024-06-07 09:45:57', 'succeeded', '', '', '', '', '', ''),
(8, 18, 1, 'cus_QFbVhOJCEcUrkG', 'price_1PP6IhLuEqhyf17nnezmmSPX', 'pi_3PP6IiLuEqhyf17n0bCAhU67', 'sub_1PP6IiLuEqhyf17nxQY2lx6w', 'pm_1PP6IjLuEqhyf17nej5dpPKT', NULL, 5.00, 'usd', 'month', 1, 'Mone V', 'gametest826@gmail.com', '2024-06-07 17:10:04', '2024-07-07 17:10:04', '2024-06-07 17:10:04', 'succeeded', '', '', '', '', '', ''),
(9, 15, 1, 'cus_QJurnRDCBbzFOB', 'price_1PTH1ZLuEqhyf17nrIs3YXdF', 'pi_3PTH1aLuEqhyf17n1n3xquem', 'sub_1PTH1aLuEqhyf17nOHp7Frav', 'pm_1PTH1bLuEqhyf17nwU984HxH', NULL, 5.00, 'usd', 'month', 1, 'Jay Mirando', 'customer2@gmail.com', '2024-06-19 05:25:38', '2024-07-19 05:25:38', '2024-06-19 05:25:38', 'succeeded', '', '', '', '', '', ''),
(10, 15, 1, 'cus_QJuuWm2CmUIhXc', 'price_1PTH4lLuEqhyf17n6uaU8XGh', 'pi_3PTH4mLuEqhyf17n0KeMORwM', 'sub_1PTH4lLuEqhyf17nMOcPLFrZ', 'pm_1PTH4nLuEqhyf17nS4YePUAp', NULL, 5.00, 'usd', 'month', 1, 'Jay Mirando', 'customer2@gmail.com', '2024-06-19 05:28:55', '2024-07-19 05:28:55', '2024-06-19 05:28:55', 'succeeded', 'Richmond', 'Richmond', '222222', 'Test', 'Richmond', 'Richmond');

-- --------------------------------------------------------

--
-- Table structure for table `view_history`
--

CREATE TABLE `view_history` (
  `id` int(11) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `user` int(11) NOT NULL DEFAULT 0 COMMENT '0 : Fixed Price, 1 : Percentage',
  `property` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `view_history`
--

INSERT INTO `view_history` (`id`, `created_at`, `user`, `property`) VALUES
(27, '2024-06-26 16:20:58', 20, 1),
(28, '2024-06-26 16:23:52', 20, 2),
(29, '2024-06-26 16:24:02', 20, 1),
(30, '2024-06-26 16:35:08', 20, 1),
(31, '2024-06-26 16:35:22', 20, 9),
(32, '2024-06-27 01:13:09', 20, 2),
(33, '2024-06-27 01:28:28', 15, 1),
(34, '2024-06-27 01:29:39', 15, 1),
(35, '2024-06-27 01:39:45', 13, 1),
(36, '2024-06-27 03:17:33', 13, 1),
(37, '2024-06-27 03:34:07', 13, 1),
(38, '2024-06-27 03:36:55', 13, 1),
(39, '2024-06-27 03:38:05', 13, 1),
(40, '2024-06-27 03:38:22', 13, 1),
(41, '2024-06-27 03:38:56', 13, 1),
(42, '2024-06-27 03:41:22', 13, 1),
(43, '2024-06-27 03:41:37', 13, 1),
(44, '2024-06-27 03:43:06', 13, 3),
(45, '2024-06-27 03:43:28', 13, 3),
(46, '2024-06-27 04:41:01', 13, 6),
(47, '2024-06-27 04:42:25', 13, 6),
(48, '2024-06-27 04:42:35', 13, 4),
(49, '2024-06-27 14:40:37', 13, 2),
(50, '2024-06-27 14:40:45', 13, 2),
(51, '2024-06-27 15:57:22', 13, 2),
(52, '2024-06-27 16:13:00', 13, 2),
(53, '2024-06-27 16:40:43', 13, 2),
(54, '2024-06-27 16:46:37', 13, 3),
(55, '2024-06-27 18:38:48', 13, 1),
(56, '2024-06-27 18:50:11', 13, 3),
(57, '2024-07-01 16:06:17', 15, 7),
(58, '2024-07-31 03:06:25', 15, 4);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comment`
--
ALTER TABLE `comment`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `plans`
--
ALTER TABLE `plans`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `property`
--
ALTER TABLE `property`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_subscriptions`
--
ALTER TABLE `user_subscriptions`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `view_history`
--
ALTER TABLE `view_history`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `comment`
--
ALTER TABLE `comment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=620;

--
-- AUTO_INCREMENT for table `plans`
--
ALTER TABLE `plans`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `property`
--
ALTER TABLE `property`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `user_subscriptions`
--
ALTER TABLE `user_subscriptions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `view_history`
--
ALTER TABLE `view_history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
