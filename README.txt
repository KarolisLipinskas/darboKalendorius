
Programa skirta sudaryti kalendorių, kuriame nurodyta kiek laiko reikia dirbti prie kursinio darbo kiekvieną dieną.
Jei darbo atlikti neįmanoma per nurodyta laiką programa išveda, kad atlikti nepavyks.

Programa naudoja Spring Boot.

Kaip naudoti:

1.
   Į "\src\main\resources\json" aplanką įkelti "Input.json" failą arba pakeisti esančio "Input.json" failo turinį.

   Input.json failo struktūros pavyzdys:
   {
     "hours": 30,                       //kiek valandų užims projektas
     "startDate": "2023-04-26",         //darbo pradžios diena
     "deadline": "2023-05-02",          //diena iki kurios reikia atlikti darbą
     "days": [                          //užimtų dienų sąrašas
       {
         "date": "2023-04-26",          //dienos data
         "busyHours": 7                 //užimtų valandų kiekis
       },
       {
         "date": "2023-04-27",
         "busyHours": 9
       },
       {
         "date": "2023-04-28",
         "busyHours": 8
       },
       {
         "date": "2023-04-30",
         "busyHours": 6
       },
       {
         "date": "2023-05-01",
         "busyHours": 1
       }
     ]
   }

   Jei konkreti diena nenurodyta, jos laisvų valandų skaičius nustatomas 16 valandų. (Programa automatiškai atima 8 valandas miegui) 
   Jei diena nurodyta, laisvų valandų skaičius apskaičiuojamas iš 16 atėmus "busyHours". (jei busyHours >= 16 laisvų valandų skaičius = 0)

2. 
   Per pasirinkta IDE paleisti DarboKalendoriusApplication.java
   failo vieta "\src\main\java\nd\darboKalendorius"

3.
   Atidaryti naršyklėje "http://localhost:8080/input"  norint patikrinti ar gerai nuskaitė "Input.json" failą.
   Atidaryti naršyklėje "http://localhost:8080/output" norint pamatyti sudaryta kalendorių JSON formatu.

   Sudaryto kalendoriaus struktūros pavyzdys:
   {
     "doable": true,                           //ar pavyks atlikti darbą laiku
     "mode": "holidays-0%, weekends-50%",      //koks procentinis darbo krūvis yra šventinėmis dienomis ir savaitgaliais (iš viso yra 4)
     "outputDays": [                           //sudarytas kalendorius
       {
         "date": "2023-04-26",                 //dienos data
         "dayType": "Workday",                 //dienos tipas (darbo diena, savaitgalis, šventinė diena)
         "workHours": 7                        //darbo valandų kiekis
       },
       {
         "date": "2023-04-27",
         "dayType": "Workday",
         "workHours": 6
       },
       {
         "date": "2023-04-28",
         "dayType": "Workday",
         "workHours": 6
       },
       {
         "date": "2023-04-29",
         "dayType": "Weekend",
         "workHours": 7
       },
       {
         "date": "2023-04-30",
         "dayType": "Weekend",
         "workHours": 4
       },
       {
         "date": "2023-05-01",
         "dayType": "Holiday",
         "workHours": 0
       }
     ]
   }

   Jei darbo atlikti nepavyks:
   {
     "doable": false,
     "mode": null,
     "outputDays": null
   }

   visi 4 budai kaip gali būti sudarytas tvarkaraštis:
 	mode: "holidays-0%, weekends-50%"    - šventinėmis dienomis nedirbama, savaitgaliais dirbama dvigubai mažiau nei darbo dienom
	mode: "holidays-50%, weekends-50%"   - šventinėmis dienomis ir savaitgaliais dirbama dvigubai mažiau nei darbo dienom
	mode: "holidays-50%, weekends-100%"  - šventinėmis dienomis dirbama dvigubai mažiau nei darbo dienom, savaitgaliais dirbama taip pat kaip darbo dienom
	mode: "holidays-100%, weekends-100%" - šventinėmis dienomis ir savaitgaliais dirbama taip pat kaip darbo dienom
