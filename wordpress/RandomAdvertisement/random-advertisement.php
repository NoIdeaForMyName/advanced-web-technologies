<?php
/*
Plugin Name: Random Advertisement
Description: Wyświetla losowe ogłoszenie na początku każdego postu.
Version: 1.0
Author: Michal Krok & Michal Maksanty
*/

if (!defined('ABSPATH')) {
    exit; // Zabezpieczenie przed bezpośrednim dostępem do pliku
}

register_activation_hook(__FILE__, 'ra_create_table');

function ra_create_table() {
    global $wpdb;
    $table_name = $wpdb->prefix . 'random_ads';
    $charset_collate = $wpdb->get_charset_collate();

    $sql = "CREATE TABLE $table_name (
        id mediumint(9) NOT NULL AUTO_INCREMENT,
        content text NOT NULL,
        PRIMARY KEY (id)
    ) $charset_collate;";

    require_once(ABSPATH . 'wp-admin/includes/upgrade.php');
    dbDelta($sql);
}

add_action('admin_menu', 'ra_admin_menu');

function ra_admin_menu() {
    add_menu_page(
        'Random Advertisement',
        'Random Ads',
        'manage_options',
        'random-advertisement',
        'ra_admin_page',
        'dashicons-megaphone',
        100
    );
}

function ra_admin_page() {
    global $wpdb;
    $table_name = $wpdb->prefix . 'random_ads';

    // Obsługa dodawania ogłoszeń
    if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['ra_content'])) {
        $content = stripslashes($_POST['ra_content']);
        $wpdb->insert($table_name, array('content' => $content));
        echo '<div class="notice notice-success"><p>Ogłoszenie dodane!</p></div>';
    }

    // Obsługa usuwania ogłoszeń
    if (isset($_GET['delete_ad']) && is_numeric($_GET['delete_ad'])) {
        $ad_id = intval($_GET['delete_ad']);
        $wpdb->delete($table_name, array('id' => $ad_id));
        echo '<div class="notice notice-success"><p>Ogłoszenie usunięte!</p></div>';
    }

    $ads = $wpdb->get_results("SELECT * FROM $table_name");

    echo '<div class="wrap">';
    echo '<h1>Random Advertisement</h1>';
    echo '<form method="post">';
    echo '<textarea name="ra_content" rows="5" cols="50"></textarea><br>';
    echo '<input type="submit" value="Dodaj ogłoszenie" class="button button-primary">';
    echo '</form>';

    echo '<h2>Lista ogłoszeń</h2>';
    echo '<ul>';
    foreach ($ads as $ad) {
        echo '<li>';
        echo wp_kses_post($ad->content);
        echo ' <a href="' . admin_url('admin.php?page=random-advertisement&delete_ad=' . $ad->id) . '" class="button button-secondary" onclick="return confirm(\'Czy na pewno chcesz usunąć to ogłoszenie?\')">Usuń</a>';
        echo '</li>';
    }
    echo '</ul>';

    // Dodanie przycisków eksportu i importu
    echo '<h2>Eksport i import ogłoszeń</h2>';
    echo '<form method="post" action="' . admin_url('admin-post.php') . '">';
    echo '<input type="hidden" name="action" value="ra_export_ads">';
    echo '<input type="submit" value="Eksportuj ogłoszenia do CSV" class="button button-secondary">';
    echo '</form>';

    echo '<form method="post" action="' . admin_url('admin-post.php') . '" enctype="multipart/form-data">';
    echo '<input type="hidden" name="action" value="ra_import_ads">';
    echo '<input type="file" name="ra_import_file">';
    echo '<input type="submit" value="Importuj ogłoszenia z CSV" class="button button-secondary">';
    echo '</form>';

    echo '</div>';
}

add_filter('the_content', 'ra_display_advertisement');

function ra_display_advertisement($content) {
    if (is_single()) {
        global $wpdb;
        $table_name = $wpdb->prefix . 'random_ads';
        $ad = $wpdb->get_row("SELECT * FROM $table_name ORDER BY RAND() LIMIT 1");

        if ($ad) {
            $ad_content = '<div class="random-advertisement">' . wp_kses_post($ad->content) . '</div>';
            $content = $ad_content . $content;
        }
    }

    return $content;
}

add_action('admin_post_ra_export_ads', 'ra_export_ads');

function ra_export_ads() {
    global $wpdb;
    $table_name = $wpdb->prefix . 'random_ads';
    $ads = $wpdb->get_results("SELECT * FROM $table_name");

    header('Content-Type: text/csv');
    header('Content-Disposition: attachment; filename="ads_export.csv"');

    $output = fopen('php://output', 'w');
    fputcsv($output, array('ID', 'Content'));

    foreach ($ads as $ad) {
        fputcsv($output, array($ad->id, $ad->content));
    }

    fclose($output);
    exit;
}

add_action('admin_post_ra_import_ads', 'ra_import_ads');

function ra_import_ads() {
    global $wpdb;
    $table_name = $wpdb->prefix . 'random_ads';

    if (isset($_FILES['ra_import_file'])) {
        $file = $_FILES['ra_import_file']['tmp_name'];
        $handle = fopen($file, 'r');

        if ($handle !== FALSE) {
            fgetcsv($handle); // Pominięcie nagłówka

            while (($data = fgetcsv($handle, 1000, ',')) !== FALSE) {
                $wpdb->insert($table_name, array(
                    'id' => $data[0],
                    'content' => $data[1]
                ));
            }

            fclose($handle);
            echo '<div class="notice notice-success"><p>Ogłoszenia zaimportowane!</p></div>';
        } else {
            echo '<div class="notice notice-error"><p>Błąd podczas otwierania pliku.</p></div>';
        }
    } else {
        echo '<div class="notice notice-error"><p>Nie wybrano pliku do importu.</p></div>';
    }

    wp_redirect(admin_url('admin.php?page=random-advertisement'));
    exit;
}

add_action('wp_enqueue_scripts', 'ra_enqueue_styles');

function ra_enqueue_styles() {
    wp_enqueue_style('random-advertisement-style', plugins_url('style.css', __FILE__));
}
