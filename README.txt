			Tema 1 - Limbaje formale si automate
			~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	1) Credentiale

	Tema a fost realizata de Daniel Berbece, student la Automatica si
Calculatoare, anul 3, grupa 335CA.

	2) Cerinta temei

	Tema a constat in realizarea unui Parser pentru limbajul IMP, un limbaj
simplist de programare, care sa citeasca un fisier sursa IMP si sa creeze doua
fisiere: arbore, in care este afisat AST-ul (Abstract Sintactic Tree), si
output, fisier in care sunt afisate variabilele in starea finala, dupa rularea
programului. Parserul a fost realizat folosind JFlex 1.7 si Java 8. In cazul 
in care exista o eroare in cod de tipul UnassignedVar sau DivideByZero,
programul o va afisa, impreuna cu linia la care a fost gasita.

	3) Surse si implementare

	Tema are la baza 3 fisiere:
	- Homework.flex, cod in limbajul jflex, din care rezulta Lexerul pe care
se bazeaza Parserul
	- Parser.java, contine functia main a Parserului: Deschide si citeste
fisierul de intrare, apeleaza lexer.yylex() si scrie in fisierele de iesire.
	- types.java, contine mai multe clase auxiliare, folosite la crearea
AST-ului si la interpretare. Aceste clase auxiliare descriu fiecare nod din
AST. (ex: PlusNode, VariableNode, WhileNode etc.)

	Parsarea foloseste la baza o stiva. De fiecare data cand intalneste un
simbol de tipul '(', '{', ',', '+', '/', '>', etc. programul adauga aceste
simboluri pe stiva. Atunci cand gasim un simbol pereche, de ex: ')', atunci
cautam simbolul initial in stiva si alcatuim un nod nou, in cazul asta BracketNode.
Pe masura ce scoatem elemente de pe stiva, cautam sa formam noduri, deoarece intre
doua paranteze putem avea mai multe operatii si operanzi. 
	Singurele simboluri pentru care scoatem de pe stiva si formam noduri sunt
')', '}' si ';'. Ce gasim in stiva sunt operatii aritmetice si boolene (Plus, Div,
Greater, Not, etc), simboluri de bucle (while), simboluri de decizie (if, else) si 
un nod secventa (sau nodul Main). Cand se formeaza un nou nod de secventa, se
verifica ce contine stiva anterior. Daca avem un simbol "while", atunci cream un
WhileNode. Daca contine o pereche "if"-"else", atunci cream un IfNode. Daca inainte
se afla doar un alt nod secventa, nodul curent se leaga de nodul secventa anterior.
Acela este scos de pe stiva si este adaugat noul nod curent.
	In cazul variabilelor, nu punem numele variabilei sub forma de simbol in
stiva, ci cream un VariableNode pe care il adaugam in stiva. La fel si in cazul 
valorilor numerice sau al celor boolene.
 
	O problema a presupus crearea in ordinea corecta a AST-ului pentru expresii
matematice. Aceasta piedica s-a rezolvat bazandu-se doua presupuneri:
	* Operatia de impartire este mai importanta decat operatia de adunare si
are loc doar intre 2 sau mai multi operanzi in lant, asemenea celei de adunare.
	* Operatia logica '>' este mai importanta decat operatia logica '&&' si are 
loc doar intre 2 operanzi in lant, pe cand operatia '&&' poate avea mai multi
operanzi in lant.
	Prin urmare, am putut rezolva problema cu 4 iteratii prin expresie: Prima
in care rezolvam impartirile, a doua in care rezolvam si cream nodurile de adunare,
a treia in care rezolvam operatiile '>' si inca o trecere in care rezolvam
operatiile logice '&&', creand nodurile necesare la fiecare trecere.

	Dupa construirea AST-ului, interpretarea a fost usoara deoarece are la baza
acest AST. Partea mai dificila a fost raportarea erorilor, intrucat erorile puteau
fi atat la compilare, cat si la rulare, asa ca a fost nevoie de o verificare a AST-ului
inainte de a incerca interpretarea (din motive ca unele erori puteau sa apara in portiuni
de cod care nu aveau sa fie rulate), dar si verificare in timpul rularii.
	Pentru a putea raporta si linia la care se produce eroarea, am adaugat fiecarui
nod o variabla "line" care contine linia din cod la care se afla respectiva operatie. 
In timpul parsarii fisierului de intrare, o variabila contine linia curenta la care 
se afla lexerul, iar atunci cand cream un nod copiem aceasta valoare in variabila 
"line" a nodului.

	Pentru mai multe detalii legate de implementare, va sugerez citirea codului
sursa mentionat anterior.

	4) Observatii

	Compilarea si rularea se pot face folosind make; make run; make clean. La
rulare este necesar sa existe un fisier 'input' in directorul curent in care sa se
gaseasca codul IMP.
