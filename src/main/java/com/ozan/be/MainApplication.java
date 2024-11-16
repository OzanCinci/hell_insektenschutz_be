package com.ozan.be;

import com.ozan.be.management.repository.DiscountRepository;
import com.ozan.be.product.repository.ProductRepository;
import com.ozan.be.product.service.PredefinedWindowsService;
import com.ozan.be.user.service.UserService;
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
      ProductRepository productRepository,
      UserService userService,
      DiscountRepository discountRepository,
      PredefinedWindowsService predefinedWindowsService) {
    return args -> {
      // predefinedWindowsService.readFromCsvAndFillDB();
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

      /*

      Product product8 = new Product();
      product8.setName("Dachfenster Plissee");
      product8.setCategory("Plissee");
      product8.setDescription("Mit einem Dachfenster-Plissee schaffen Sie eine stilvolle und funktionale Lösung für Sonnenschutz und Verdunkelung an Dachfenstern. Die maßgefertigten Dachfenster-Plissees lassen sich ideal an Ihre Fenster sowie Ihre ästhetischen Ansprüche anpassen. Wir bemühen uns, die Farben so realitätsnah wie möglich darzustellen. Dennoch können Stoffe je nach Tageszeit und Lichteinfall variieren. Um sicherzustellen, dass der Stoff Ihren Vorstellungen entspricht, empfehlen wir, vorab kostenlose Muster anzufordern.");

      productRepository.saveAndFlush(product8);



      Product product9 = new Product();
      product9.setName("Sonderformen Plissee");
      product9.setCategory("Plissee");
      product9.setDescription("Auch bei ungewöhnlichen Fensterformen sind Sie bei uns genau richtig. Ob schräge, drei- oder fünfeckige Fenster – wir haben die passende Lösung. Unsere Plissees bieten nicht nur Sonnen- und Hitzeschutz, sondern verschönern Ihre Fenster mit einem modernen Stil in erfrischenden Farben und Designs. Wir bemühen uns, die Farben möglichst genau darzustellen, doch je nach Tageszeit und Lichteinfall können Abweichungen auftreten. Zur Sicherheit empfehlen wir, vorab kostenlose Muster anzufordern, um sicherzustellen, dass der Stoff Ihren Vorstellungen entspricht.");

      productRepository.saveAndFlush(product9);




      Product product10 = new Product();
      product10.setName("Smart Akku Plissee");
      product10.setCategory("Plissee");
      product10.setDescription("Plissees und Wabenplissees vereinen Stil und Funktion, bringen mit frischen Farben Wohnlichkeit und schützen vor Sonne. Wabenplissees bieten zusätzlich Isolierung. Komfortable Bedienung per Fernbedienung, App, Timer und über WLAN mit Smart-Home-Integration möglich.");

      productRepository.saveAndFlush(product10);



      Product product11 = new Product();
      product11.setName("Freihängend Plissee");
      product11.setCategory("Plissee");
      product11.setDescription("Plissees sind vielseitige Designelemente, die mit schönen Farben und modernen Designs eine gemütliche, wohnliche Atmosphäre schaffen und mehr bieten als nur Sonnenschutz. Wabenplissees sorgen zusätzlich für Isolierung und bringen einen frischen, modernen Look ins Zuhause.");

      productRepository.saveAndFlush(product11);




      Product product12 = new Product();
      product12.setName("Lamellenvorhang Schräg");
      product12.setCategory("Lamellenvorhang");
      product12.setDescription("Schräge Lamellenvorhänge sind die perfekte Lösung für Dachschrägen, wenn herkömmliche Optionen nicht passen. Stilvoll und funktional, in gemusterten, strukturierten oder einfarbigen Stoffen, bieten sie einfache Montage. Farbabweichungen durch Licht und Tageszeit sind möglich – wir empfehlen daher, vorab kostenlose Muster zu bestellen.");

      productRepository.saveAndFlush(product12);

       */

      /*
      User user2 = userService.getUserByMail("mkinsektenschutz@outlook.de");
      User user3 = userService.getUserByMail("insektenschutz.k@gmail.com");
      User user6 = userService.getUserByMail("info@sliakas-wohndesign.de");


      Discount discount = new Discount();
      discount.setPercentage(0.4);
      discount.setDiscountType(DiscountType.DEALER);
      discount.setDiscountProductType(NOT_A_PRODUCT);
      discount.setValidUntil(null);
      discount.setActive(true);


      discount.setUser(user2);
      discountRepository.saveAndFlush(discount);


      Discount discount2 = new Discount();
      discount2.setPercentage(0.4);
      discount2.setDiscountType(DiscountType.DEALER);
      discount2.setDiscountProductType(NOT_A_PRODUCT);
      discount2.setValidUntil(null);
      discount2.setActive(true);
      discount2.setUser(user3);
      discountRepository.saveAndFlush(discount2);


      Discount discount3 = new Discount();
      discount3.setPercentage(0.4);
      discount3.setDiscountType(DiscountType.DEALER);
      discount3.setDiscountProductType(NOT_A_PRODUCT);
      discount3.setValidUntil(null);
      discount3.setActive(true);
      discount3.setUser(user6);
      discountRepository.saveAndFlush(discount3);

       */

      /*
      Instant firstOfDecember = LocalDate.of(2024, 12, 1)  // Set the date to December 1, 2024
              .atStartOfDay(ZoneOffset.UTC) // Start of day at UTC
              .toInstant();

      Discount discount = new Discount();
      discount.setPercentage(0.2);
      discount.setDiscountType(DiscountType.PUBLIC);
      discount.setDiscountProductType(DiscountProductType.NOT_A_PRODUCT);
      discount.setValidUntil(firstOfDecember);
      discount.setActive(true);
      discountRepository.saveAndFlush(discount);
       */
    };
  }
}
