package de.campusshop.init;

import de.campusshop.model.AppUser;
import de.campusshop.model.Category;
import de.campusshop.model.Product;
import de.campusshop.model.Role;
import de.campusshop.repository.AppUserRepository;
import de.campusshop.repository.CategoryRepository;
import de.campusshop.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

/**
 * Legt Beispiel-Daten an (nur wenn die Tabellen leer sind).
 * - Default-User: admin/admin123 und user/user123
 * - Kategorien + Produkte
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private static final String SLUG_CLOTHING = "kleidung";
    private static final String SLUG_STATIONERY = "schreibwaren";
    private static final String SLUG_MUGS = "tassen";

    @Bean
    CommandLineRunner initData(ProductRepository productRepository,
                               CategoryRepository categoryRepository,
                               AppUserRepository appUserRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {

            createDefaultUsersIfEmpty(appUserRepository, passwordEncoder);
            createDefaultCategoriesIfEmpty(categoryRepository);
            createDefaultProductsIfEmpty(productRepository, categoryRepository);
        };
    }

    private void createDefaultUsersIfEmpty(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        if (appUserRepository.count() > 0) {
            log.info("User existieren bereits – keine neuen angelegt.");
            return;
        }

        AppUser admin = new AppUser(
                "admin",
                passwordEncoder.encode("admin123"),
                Role.ADMIN,
                true
        );

        AppUser user = new AppUser(
                "user",
                passwordEncoder.encode("user123"),
                Role.USER,
                true
        );

        appUserRepository.save(admin);
        appUserRepository.save(user);

        log.info("Default-User angelegt: admin/admin123 und user/user123");
    }

    private void createDefaultCategoriesIfEmpty(CategoryRepository categoryRepository) {
        if (categoryRepository.count() > 0) {
            log.info("Kategorien existieren bereits – keine neuen angelegt.");
            return;
        }

        categoryRepository.save(new Category("Kleidung", SLUG_CLOTHING));
        categoryRepository.save(new Category("Schreibwaren", SLUG_STATIONERY));
        categoryRepository.save(new Category("Tassen", SLUG_MUGS));

        log.info("Beispielkategorien wurden angelegt.");
    }

    private void createDefaultProductsIfEmpty(ProductRepository productRepository,
                                              CategoryRepository categoryRepository) {
        if (productRepository.count() > 0) {
            log.info("Produkte existieren bereits – keine neuen angelegt.");
            return;
        }

        Category clothing = findCategoryBySlugOrThrow(categoryRepository, SLUG_CLOTHING);
        Category stationery = findCategoryBySlugOrThrow(categoryRepository, SLUG_STATIONERY);
        Category mugs = findCategoryBySlugOrThrow(categoryRepository, SLUG_MUGS);

        productRepository.save(new Product(
                "Campus Hoodie",
                "Warmer Hoodie mit Campus-Logo. Ideal für kalte Tage in der Bibliothek.",
                new BigDecimal("39.99"),
                50,
                true,
                "/images/campus-hoodie.jpg",
                clothing
        ));

        productRepository.save(new Product(
                "Campus Notizbuch",
                "A5-Notizbuch mit karierten Seiten und stabilem Einband.",
                new BigDecimal("4.99"),
                200,
                true,
                "/images/campus-notizbuch.jpg",
                stationery
        ));

        productRepository.save(new Product(
                "Campus Kaffeebecher",
                "Keramikbecher (300ml) mit Campus-Branding. Spülmaschinenfest.",
                new BigDecimal("9.99"),
                120,
                true,
                "/images/campus-becher.jpg",
                mugs
        ));

        productRepository.save(new Product(
                "Campus Kugelschreiber",
                "Blauer Kugelschreiber (Mine austauschbar). Perfekt für Prüfungen.",
                new BigDecimal("1.49"),
                500,
                true,
                "/images/campus-kugelschreiber.jpg",
                stationery
        ));

        log.info("Beispielprodukte wurden angelegt (mit Kategorien).");
    }

    private Category findCategoryBySlugOrThrow(CategoryRepository repo, String slug) {
        // Besser wäre: repo.findBySlug(slug) als Repository-Methode
        return repo.findAll().stream()
                .filter(c -> slug.equals(c.getSlug()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Kategorie '" + slug + "' fehlt"));
    }
}
