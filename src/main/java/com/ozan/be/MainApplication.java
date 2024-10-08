package com.ozan.be;

import static com.ozan.be.user.domain.Role.*;

import com.ozan.be.auth.AuthenticationService;
import com.ozan.be.order.*;
import com.ozan.be.product.ProductRepository;
import com.ozan.be.product.ProductService;
import com.ozan.be.review.ReviewRepository;
import com.ozan.be.review.ReviewService;
import com.ozan.be.user.UserRepository;
import com.ozan.be.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class MainApplication {

  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(
      AuthenticationService service,
      UserRepository userRepository,
      OrderService orderService,
      ProductRepository productRepository,
      OrderItemRepository orderItemRepository,
      ReviewService reviewService,
      ProductService productService,
      ReviewRepository reviewRepository,
      UserService userService) {
    return args -> {
      /*
           Product product = new Product();
           product.setName("Basic Plissee");
           product.setCategory("Plissee");
           product.setDescription(
               "Basic Plissee – der kostengünstige, aber hochwertige Sonnenschutz für Ihre Fenster. Eine große Auswahl an Farben und Eigenschaften - von Sichtschutz bis Hitzeschutz - hier werden Sie sicherlich fündig. Wir bemühen uns, die Farben so genau wie möglich darzustellen. Je nach Tageszeit und Lichteinfall können die Stoffe jedoch unterschiedlich wirken. Um sicherzustellen, dass der Stoff Ihren Vorstellungen entspricht, empfehlen wir, vorab kostenlose Muster zu bestellen.");
           Product savedProduct = productRepository.saveAndFlush(product);
           System.out.println(savedProduct.getId());


           Product product2 = new Product();
           product2.setName("Premium Plissee");
           product2.setCategory("Plissee");
           product2.setDescription(
                   "Die Premium Plissees zeichnen sich durch ihre ausgeklügelte Technik – Made in Germany – aus. Mit einer großen Auswahl an trendigen Farben und Designs schaffen sie eine gemütliche Atmosphäre in Ihrem Zuhause. Die einfache Bedienung und die funktionalen Stoffe bieten einen großen Mehrwert und sorgen für lange Freude am Produkt. Wir bemühen uns, die Farben so genau wie möglich darzustellen. Aufgrund der Lichtverhältnisse zu verschiedenen Tageszeiten können die Stoffe jedoch unterschiedlich wirken. Um sicherzustellen, dass der Stoff Ihren Erwartungen entspricht, empfehlen wir, vorab kostenlose Muster zu bestellen");
           Product savedProduct2 = productRepository.saveAndFlush(product2);
           System.out.println(savedProduct2.getId());



           Product product22 = new Product();
           product22.setName("Basic Jalousie");
           product22.setCategory("Jalousie");
           product22.setDescription(
                   "Farboptionen für Basic Alu Jalousien 25mm: Die 25 mm breiten Lamellen sind die am häufigsten gewählte Größe und zählen zu unseren meistverkauften Produkten. Mit diesen Jalousien erzielen Sie mühelos optimalen Sicht- und Blendschutz. Eine ideale Wahl, um sowohl Ihre Küche als auch Ihr Wohnzimmer stilvoll zu gestalten.");
           Product savedProduct22 = productRepository.saveAndFlush(product22);

           Product product21 = new Product();
           product21.setName("Premium Jalousie");
           product21.setCategory("Jalousie");
           product21.setDescription(
                   "Farboptionen für HD Premium Alu Jalousien 25mm: Die Premium Jalousie \"Ultimate\" besticht durch ihre hochwertigen Materialien und die langlebige Verarbeitung. Die Bedienung erfolgt wahlweise mit Schnur und Wendestab oder mit Kette - einfach und praktisch. Ein zeitloser Klassiker im Bereich des Sonnenschutzes, der optimalen Sicht- und Blendschutz bietet und stufenlos mit nur einer Handbewegung einstellbar ist.");

           productRepository.saveAndFlush(product21);


           Product product29 = new Product();
           product29.setName("Basic Lamellenvorhang");
           product29.setCategory("Lamellenvorhang");
           product29.setDescription(
                   "Ob zu Hause oder im Büro, wir sehnen uns nach hellen, lichtdurchfluteten Räumen. Große Fensterflächen machen dies möglich. Dennoch benötigen wir Sichtschutz, blendfreies Arbeiten und Dunkelheit für erholsamen Schlaf – all das bieten Lamellenvorhänge stilvoll und funktional.");

           productRepository.saveAndFlush(product29);



           Product product25 = new Product();
           product25.setName("Premium Lamellenvorhang");
           product25.setCategory("Lamellenvorhang");
           product25.setDescription(
                   "Der AEROLUX-PREMIUM-Lamellenvorhang bietet eine verbesserte Langlebigkeit bei häufiger Nutzung. Für Privatsphäre, Sonnenschutz und dunkle Räume für einen erholsamen Schlaf sind diese stilvollen und funktionalen Lamellenvorhänge die ideale Wahl. Wir bemühen uns, die Farben so genau wie möglich darzustellen, jedoch können Stoffe je nach Tageszeit und Lichteinfall variieren. Um sicherzustellen, dass der Stoff Ihren Erwartungen entspricht, empfehlen wir, kostenlose Muster im Voraus zu bestellen.");

           productRepository.saveAndFlush(product25);



           Product product3 = new Product();
           product3.setName("Premium Rollo");
           product3.setCategory("Rollo");
           product3.setDescription(
                   "Seit dreißig Jahren stehen Rollos für Qualität und Innovation. Sie haben die Wahl zwischen Bedienung mit oder ohne Kette oder mit einem Akkumotor, möglicherweise ergänzt durch eine Designkassette, die den aufgerollten Stoff stilvoll verdeckt. Zusätzlich bieten wir eine Auswahl von hunderten Stoffen mit funktionalen Eigenschaften, die perfekt zu Ihnen passen. Unsere Premium Rollos verleihen jedem Raum eine gemütliche Atmosphäre. Wir bemühen uns, die Farben so realistisch wie möglich darzustellen, aber Stoffe können je nach Tageszeit und Lichteinfall variieren. Um sicherzustellen, dass der Stoff Ihren Erwartungen entspricht, empfehlen wir, kostenlose Muster vorab zu bestellen.");

           productRepository.saveAndFlush(product3);


           Product product4 = new Product();
           product4.setName("Doppel Rollo");
           product4.setCategory("Rollo");
           product4.setDescription(
                   "Seit dreißig Jahren stehen Doppelrollos für Qualität und Innovation. Sie können zwischen Ketten- oder Akkumotorbedienung wählen, möglicherweise ergänzt durch eine Designkassette, die den aufgerollten Stoff stilvoll verdeckt. Duo-Rollo sind auf zwei parallel verlaufenden Stoffbahnen transparente und halbtransparente Streifen angeordnet, die flexibel einstellbar sind. So können Sie entweder volles Tageslicht genießen oder eine abdunkelnde Einstellung wählen. Wir bemühen uns, die Farben so realistisch wie möglich darzustellen, doch je nach Tageszeit und Lichteinfall können Stoffe unterschiedlich wirken. Um sicherzustellen, dass der Stoff Ihren Erwartungen entspricht, empfehlen wir, kostenlose Muster vorab zu bestellen.");

           productRepository.saveAndFlush(product4);


           Product product5 = new Product();
           product5.setName("Basic Rollo");
           product5.setCategory("Rollo");
           product5.setDescription(
                   "Ein Rollo verleiht jedem Raum eine gemütliche Atmosphäre. Durch einfaches Auf- und Abrollen bieten sie mehr Privatsphäre, Sonnen- und Hitzeschutz sowie moderne Farbakzente. Die maßgefertigten Rollos lassen sich sehr einfach montieren, sind leicht zu reinigen und bieten eine große Vielfalt an Stoffdesigns und Funktionen. Wir bemühen uns, die Farben so realistisch wie möglich darzustellen, aber je nach Tageszeit und Lichteinfall können sie unterschiedlich wirken. Um sicherzustellen, dass der Stoff Ihren Erwartungen entspricht, empfehlen wir, kostenlose Muster vorab zu bestellen.");

           productRepository.saveAndFlush(product5);

           Product product6 = new Product();
           product6.setName("Wintergarten Plissee");
           product6.setCategory("Plissee");
           product6.setDescription("Unangenehme Hitze im Wintergarten ist nun Vergangenheit. Mit diesen speziellen Plissees schaffen Sie einen eleganten Sonnen- und Hitzeschutz und können Ihren Wintergarten wieder optimal nutzen. Auch mit diesen speziellen Wabenplissees gehört unangenehme Hitze im Wintergarten der Vergangenheit an. Sie bieten eleganten Sonnen- und Hitzeschutz und ermöglichen die optimale Nutzung Ihres Wintergartens. Wir versuchen, die Farben bestmöglich darzustellen, aber je nach Tageszeit und Lichteinfall können Stoffe variieren. Um sicherzustellen, dass der Stoff Ihren Vorstellungen entspricht, empfehlen wir, vorab kostenlose Muster zu bestellen.");

           productRepository.saveAndFlush(product6);


           Product product7 = new Product();
           product7.setName("Holzjalousie");
           product7.setCategory("Jalousie");
           product7.setDescription("Holzjalousien verleihen Wohnräumen eine besondere Atmosphäre. Dieser Sicht- und Sonnenschutz ist eine moderne Hommage an traditionelle Fensterläden. Gemütlich, stilvoll und individuell, sind Holzjalousien sowohl funktional als auch ästhetisch. Holzjalousien aus Echtholz mit 25-50 mm breiten Lamellen bieten eine warme, einladende Optik und geben den Wohnräumen eine behagliche Note. Ihr geradliniges Design macht sie zugleich ästhetisch und funktional. Jalousien aus edlem Lindenholz bringen eine elegante Atmosphäre in Ihr Zuhause. Die optische Wärme des Holzes passt perfekt zu klassischem Wohndesign, kann aber auch als Kontrast zu moderner und geradliniger Inneneinrichtung genutzt werden.");

           productRepository.saveAndFlush(product7);
      */

    };
  }
}
