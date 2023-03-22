# ProjetoDecoder - Aprendizados da semana 1
Este repositório tem como objetivo deixar registrado o que eu aprendi em cada aula do Projeto Decoder. A Cada semana finalizada do treinamento devo subir minhas anotações em markdown para fixar os conceitos aprezentados durante as aulas e também é uma de aprender formatações com Markdown. 


JSON VIEW - Múltiplas Visualizações em API

É usado para limitar ou controlar a exibição de dados para diferentes clientes da api. Ao invés de criar um DTO específico para cada resposta afim de atender cada cliente, o uso de view permite fazer diferentes representações a partir de um único DTO.
O contexto é para um cenário em que seja necessário atualizar diferentes campos de um domínio ou objeto do domínio ou um campo específico.
Um DTO(Data Transfer Object) é utilizado para mapear e transferir geralmente os dados da camada de visão para a camada de persistência, é uma maneira muito utilizada dentro da orientação a objetos para transferir dados de um objeto para outro dentro da aplicação.
Ponto de atenção: se houver diversos endpoints na api e para cada um ser necessário ter uma View diferente, o DTO pode ficar com diversas anotações a fim de representar cada uma das diferentes visões. Pode haver um cenário em que tenha muitaS anotações.

Como implementar?
Para Implementar JsonView na API é preciso fazer uso de interfaces e anotações a fim de dividir os campos de um mesmo DTO para diferentes validações (dados de um usuários a serem salvos, dados de usuário para atualização, senha, imagem de perfil)
Em um DTO é preciso declarar uma interface (Também pode ser declarado fora da classe) e dentro da interface declarar outras interface de forma estática.

código da interface UserView

Usar a anotação @JSonView nos atributos que serão visualizados ou que poderão sofrer alterações de acordo com o cenário. Esta anotação possui um argumento que pode receber uma ou mais interfaces(.class) estáticas declaradas no DTO. 

código do DTO UserDTO - destacar onde as views são usadas de acordo com a regra de negócio

Cada uma das interfaces representa uma regra de negócio do recurso para alterações de um recurso. 

explicar no doc markdown cada uma das interfaces definidas
RegistrationPost -> são os dados necessários para salvar um usuário. 
UserPut -> atualizar alguns dados de usuário
PasswordPut -> atualizar a senha do usuário
Image Put -> atualizar a foto de perfil do usuário



É preciso passar no argumento da anotação a(s) interface(s) estática(s) que será(o) responsável(is) por permitir a visualização daquele(s) atributo(s) na API.

Regra de negócio

Registro de usuário. Para fazer um registro é preciso salvar:
userName
email
password
fullName 
phoneNumber
cpf

No DTO 
É preciso usar a anotação @JsonView e como argumento chamar a interface estática que irá representar a visão ou view(um mesmo atributo pode ser visto em outras visões, nesse caso deve ser passado um array das interfaces que representam cada visão que o atributo do DTO será usado).

No Controller
De acordo com a regra de negócio (para este contexto é cadastro de um usuário) devem ser usadas as views definidas no DTO nos endpoints definidos (no controller que atende a requisição de salvar um registro. Para isso, o método responsável por atender a requisição que salva um registro deve estar configurado para esse cenário. É preciso usar a interface estática definida no DTO.

Essas validações de JSON também podem ser usadas nas anotações @NotBlank, @Size usando a variável “groups” destas anotações.

As views também podem ser usadas em anotações do Spring Validator para o caso de apenas uma view ser necessária como argumento da anotação. Para serem usadas no Controller como é feito no com @JSonView é preciso passar no argumento da anotação o mesmo valor usado no argumento da anotação @JsonView.

Explica o regra de negócio para cada interface?

Essas validações de JSON também podem ser usadas nas anotações @NotBlank, @Size usando a variável “groups” destas anotações.

As views também podem ser usadas em anotações do Spring Validator para o caso de apenas uma view ser necessária como argumento da anotação. Para serem usadas no Controller como é feito no com @JSonView é preciso passar no argumento da anotação o mesmo valor usado no argumento da anotação @JsonView. (isso no contexto de cadastro de usuário)

Essas anotações do JSON também podem serem usadas nas anotações @NotBlank, @Size usando a variável “groups” destas anotações

As views também podem ser usadas em anotações do Spring Validator para caso de apenas uma visão basta passar como um argumento da anotação para casos onde o campo deve ser validado em mais de um cenário específico pode se usar uma array nas anotações do Spring Validator e deve ser passado um array (igual as demais anotações) na variável “groups” no argumentos da anotação.
Para serem usadas no controller iguais a anotação @JSonView e uso só precisa passar no argumento da anotação @Validated o mesmo valor usada na anotação @JsonView

Constraints em Java
Preciso criar uma interface com nome apropriado.
A palavra chave deve ser escrita com “@” antes -> @interface.
Declarar a classe que implementa a interface ConstraintValidator<A extends Annotation, T>. Onde A é annotation criada e T é tipo de atributo a ser validado.  
implementar os métodos da interface
O primeiro método tipo sem retorno (initialize) recebe como argumento a anotação declarada anteriormente e é repassada ao construtor super da classe Constraintvalidator, ver o código. Neste método não é necessário mexer.
O segundo método (isValid) irá conter a regra de negócio para validar o atributo onde a notação será usada.

@Constraint atenção aqui, está classe contém um parâmetro chamado validatedBy que irá receber metadados da classe (.class) que implementa a interface ConstraintValidator
@Target é para dizer onde a anotação pode ser usada. Ex:  retorno de método ou campos
@Retention é para dizer quando a anotação será usada em tempo de execução.

Paginação

	 no método Jet responsável por estar todos os usuários deve ser incluindo a anotação  @PageableDefault, Ao mesmo tempo é preciso inicializar alguns parâmetros desta anotação.
Page: É o número inicial da página, o ideal é começar com 0. Um exemplo de uso da paginação e a listagem de itens cadastrados nem sempre são de estado todos os itens apenas alguns de forma que estão separados em páginas.
Size: Este parâmetro indica a quantidade de elementos dentro da lista.
Sort: Este parâmetro é usado para fazer a ordenação através de algum atributo comum a todos os elementos na página, Para este exemplo o atributo usado para ordenação é o userId.
Direction: Este parâmetro irá definir a forma de ordenação ou seja ordenação ascendente que é do menor para o maior ou ordenação descendente que é do maior para o menor
Olha o que eu descobri é possível fazer digitalização apenas com a voz e o Google consegue escrever um texto O que eu tenho anotado em caderno desde que eu diga para ele muito f*** né descobri é possível fazer digitalização apenas com a voz que o Google consegue escrever um texto que eu tenho anotado em caderno desde que eu diga para ele muito f*** né

Mudanças necessárias
Na interface de UserService é preciso declarar o método.
na implementação de UserService também é preciso dar corpo ao método
No UserRepository é preciso declarar um método que tenha como argumento um Pageable (pacote org.springframework.data.domain.Pageable). O tipo de retorno na declaração do método deve ser uma lista do tipo Page<T> onde T é o tipo ou entidade para a classe que representa os dados retornados do banco. Neste caso o tipo ter retornado UserModel -> Page<UserModel>

Passagem de valores para os parâmetros de Paginação
Para definir os parâmetros da paginação é preciso definir os Esta classe/biblioteca irá converter os dados dos parâmetros passados para tipos básicos Java (em uns tipos data dente dente time números etccabeçalhos da requisição(headers) de acordo com o que foi definido No método Isto é feito no pulso na aba de headers.Os headers são conjunto Map (uma estrutura de dados do tipo chave e valor) os nomes dos atributos (de cada valor do Map) serão de acordo com o contexto -> page, size e sorte serão as chaves e terão seus valores.

É preciso ficar atento com tipo do valor a ser passado(gae e size recebem valores inteiros e sort recebe o valor creationDate,desc ou creationDate,asc(O primeiro indica que a ordenação deve ser de forma descendente e o segundo indica que a ordenação deve ser feita de forma ascendente/0.

Specification

É usado para fazer filtros de forma avançada a várias maneiras de fazer a inclusão do specifications em APIs. Permite encadear regras de negócio como uma query usada na base de dados

A motivação para o uso do especifiquei Chan é a necessidade do cliente conseguir solicitar o que deseja de acordo com filtros.

No exemplo: filtrar todos os usuários que são estudantes, são administradores, estão bloqueados e que tenham e-mail específico.

Configurar um resolver do SpringBoot
Criar o pacote de configs que será um diretório
Criar a classe de configuração do Spring e adotá-la com @Configuration (ResolverConfig)
Esta classe precisa herdar da classe WebMvcConfigurationSupport. Esta classe irá converter os dados dos parâmetros passados para tipos básicos Java (Enums, tipos data, double etc )
Sobrescrever o método add (Como argumento recebe uma lista de HandlerMethodArgumentResolver)
Usar a lista que foi passado como argumento e adicionar um novo EspecicationArgumentResolver (New SpecificationArgumentResolver()).
A paginação também precisa ser levada em conta no resolvo isto é tem que adicionar lá também como um resolver até agora só tenho especifiquei para isso é preciso declarar instanciar um objeto da classe adicionado igual o objeto resolver no especifiquei também foi adicionado. instanciar um PageableHandlerMethodArgumentResolver.
adicionar o objeto PageableHandlerMethodArgumentResolver na mesma lista que adicionou o SpecificationArgumentResolver())
Agora que eu resolver do Specification e Pageable foram adicionados é preciso adicionar à lista de Resolvers, isso é feito chamando o método addArgumentsResolvers da superclasse e passando no argumento a lista de Resolvers
Com essa configuração do WebMVC Sempre que a API receber solicitações com esses parâmetros de forma indireta irá consiga enxergar e conseguir fazer a conversão para tipos básicos Java,

Criar template do Specification
Criar um pacote Specification
Criar classe EspecificationTemplate
Declarar uma interface com o nome apropriado (para o contexto de negócio é UserSpec)
A interface precisa herdar a interface Specification (do pacote JPA) e seu tipo T precisa ser do tipo da classe DTO (usermodel)

Definição do Filtro
Para o negócio os filtros consistem em fazer buscas de usuários através de tipo de usuário(userType) status e e-mail
Para que cada um dos Filtros sejam implementados é preciso anotar a interface declarada com @Spec
Para que cada um dos filtros sejam implementados é preciso anotar a interface declarada com @Spec. Esta anotação tem dois parâmetros;
path: onde é informado o nome do parâmetro usado para filtrar (deve ser uma String)
spec = define o tipo de Specification, deve ser Igual para identificar o valor exato ou Like para identificar parcialmente. Equal é a classe que representa a operação de fazer a busca por um exato valor e Like é a classe que representa a busca a operação de fazer a busca por um valor parcialmente. Devem ser passado no argumento spec os metadados da operação desejada. Exemplo de operação para Equal -> todos os usuários que tenham o CPF de 458.547.484-20. Exemplo de operação para LIKE -> todos os usuários que tenham o nome Ronaldo.

Explicado o papel do @Spec e seus parâmetros necessários para o exemplo. Podemos definir os specifications para poder ser feio a pesquisa por e-mail, status e userStatus.
É preciso definir um conjunto de três @Spec de forma que o path seja uma string que representa o exato nome do valor do atributo e spec receba o Equal.class para que a busca retorne valores que são iguais. Por fim é preciso agrupar as @Spec agrupá-las em um array e passar este array como argumento para uma anotação chamada @And.

Notas sobre specification
“A especificação (Specification) é uma adaptação de um formalismo estabelecido, ou seja, o predicado.”
Significado de predicado: característica inerente a um ser; atributo, propriedade

Alguns entendimentos

Predicados são objetos no qual criamos para separar as regras de negócio que não cabem na entidade, ou pelo menos, não seria de bom tom escrevê-las nas entidades. Predicados são regras de negócio puro e simples que trazem um retorno booleano satisfazendo ou não uma condição.
Fonte: https://dsdumba.com/specification/

Tem relação com Open/Closed: apesar do princípio destacar que uma entidade classe deve estar aberta para extensão e fechada para modificação há cenários em que uma regra de negócio pode ser alterada ao longo do tempo. O Especification oferece uma proposta para essa situação: mover a lógica de uma regra de negócio que pode mudar ao longo do tempo
da classe/entidade que representa a regra de negócio para uma classe específica para uma regra de negócio.

Prós
Toda vez que houver uma modificação da regra de negócio basta que seja criado uma nova classe representando essa modificação. Essa abordagem permite que os testes da specification anterior continuem passando sem alterar

https://www.youtube.com/watch?v=dCdOG_RZpxU&list=PLkpjQs-GfEMN8CHp7tIQqg6JFowrIX9ve&index=9

LEMBRETE: terminado o curso, consultar o livro de DDD.

HATEOAS

Pontos sobre o HATEOAS
Nem sempre é preciso o uso de H.A.T.E.O.A.S, APIs podem funcionar normalmente sem o uso de H.A.T.E.O.A.S.
Facilita a vida do consumidor da API.
H.A.T.E.O.A.S impõe em um certo “grau” de overhead para representar recursos e de acordo com o volume de recursos que a API possui, isso pode ser significativo dependendo do volume e restrições de banda. Dada uma API com muitos dados é necessário avaliar se HATEOAS será bem aplicado nesse contexto.
Não há obrigação de H.A.T.E.O.A.S na API. Seu uso implica na maturidade da API, porém é sempre preciso ponderar o uso de H.A.T.E.O.A.S na A.P.I, porém é sempre preciso ponderar o uso de H.A.T.E.O.A.S na API.
Para fazer o recurso ser capaz de construir hyperlink é preciso que o domínio do recurso herde a classe RepresentationModel<T> onde T é a classe do domínio. A classe  RepresentationModel<T> é do pacote H.A.T.E.O.A.S.
No UserController
No endpoint que retorna todos os usuários
Fazer verificação da lista, caso não seja vazia, para cada elemento presente na lista
É chamado o método add (herdado da classe RepresentationModel), seu argumento é o resultado do método estático linkTo (da classe WebMvcLinkBuilder) que será responsável por fazer um link do Controller do recurso. Este método recebe como argumento o retorno do método estático methodOn(da classe WebMvcLinkBuilder), este método recebe como argumento uma chamada fictícia ao método de destino no controller e define o retorno do método como a variável de caminho da URI.
A partir do retorno do linkTo é chamado o método de instância withSelfRel, este método cria o link para o controller do recurso

### Principais Bibliotecas de Logging, Log Levels e Logging no Spring com Logback

Logs: basicamente são um registro de tudo que acontece na aplicação. É usado para registrar várias coisas:
aplicação iniciada, quando ocorre um erro, quando um usuário faz alguma ação, entre outras coisas. No 
desenvolvimente ajuda a entender o que está ocorrendo dentro da aplicação para corrigir depois.

- Existem 5 níveis de log:
  - debug: registra informações de depuração, auxilia no desenvovimento para o que esta ocorrendo no programa
  - info: registra informações de depuração mas que ajudam entender o que ocorrendo no programa. Ex: salva ou executar
    alguma ação
  - wargin: evisa que há algo de incosistente mas que ainda não é um erro.
  - error: registra informações de que alguma coisa deu errado na aplicação
  - trace: usado para mostrar informações muito especificas da aplicação, não é adequado de usar em produção
  por gerar muitas informações e acabar atrapalhando quando for necessário encontrar uma informação
  no log
  - **hierarquia**: há uma hierarquia entre os níveis de log que mostram o nível de importância entre eles: 
  - TRACE < DEBUG < INFO < WARN < ERROR. IMPORTANTE: se for definido mais de um nível para um pacote, o log
  de mais fina granularidade será priorizado.

- No Spring é possivel declarar níveis de log no código e para aplicação como um todo. Para aplicação deve ser declarado no ymal:
  ````yaml
  logging:
    level:
      com.ead: TRACE
      #Traz informações relevantes e úteis sem auto detalhamenteo de DEBUG
      root: INFO
      #NÍVEIS DE LOG PARA O PACOTE: QUANDO RECEBER REQUISIÇÃO O CONSOLE IRÁ EXIBIR UM DETALHAMENTO MAIOR EM INFORMAÇÕES:
      #método HTTP usado, método do controller usado, o que vem na requisição
      #e detalhes do retorno da requisição. Enquanto deselve é bom para saber o que está vindo
      #na requisição da API
      org.springframework.web: DEBUG
      #aumenta mais ainda o log exibindo informações detalahdas do hibernate
      org.hibernate: INFO
   ````

#### Implementando Logging nos Microservices com Log4J2

- É possível alterar o esquema de cores do console usando o padrão ANSI, no ymal do projeto basta
colocar esta declaracao:
  - ````yaml
    output:
      ansi:
        enabled: always
    ````
- É possível alterar o esquema de cores da MENSAGEM do log usando o padrão ANSI. Basta concatenar a cor em código
ANSI com a mensagem do Log.
  - Black: \u001b[30m.
  - Red: \u001b[31m.
  - Green: \u001b[32m.
  - Yellow: \u001b[33m.
  - Blue: \u001b[34m.
  - Magenta: \u001b[35m.
  - Cyan: \u001b[36m.
  - White: \u001b[37m.
  - Exemplo: mostra mensagem de log na cor azul
    - ````java
      log.warn("\u001b[34m" + "UserName {} is Already Taken!", userDto.getUserName());
      ````
Artigo interessante sobre cores no console: 
[How to Log to the Console in Color](https://www.baeldung.com/java-log-console-in-color)

# SEMANA 3 - API Composition Pattern e Comunicação Síncrona entre os Microservices


#### Mapear Relacionamentos para Comunicação entre Microservices 

0 MS Authuser precisa saber quando um usuário se cadastra em determindo curso (MS COURSE).
Isso é feito através do id do usuário que se cadastrou e qual que foi o curso
que ele se cadastrou. Isso vai ficar armazendo em uma tabela em authuser:

````java
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_USERS_COURSEs")
public class UserCourseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserModel user;

    @Column(nullable = false)
    private UUID courseId;

}
````

Na classe de domínío UserModel:
````java
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
private Set<UserCourseModel> usersCourses;
````
As tabelas de relacionamento estão em dois microservices diferentes, 
em base de dados diferentes, a ideia é fazer o relacionamento de ambos os lados
para correlacionar courses com users, elas foram criadas para que possamos 
consultar tanto os cursos de um usuário quanto os usuários de um curso de ambos os lados.
Esse relacionamento é uma forma de criar uma associação da referencia dos recursos entre os microservices, 
ao invés de replicar todo o recurso e ter que lidar com a consistência
