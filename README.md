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

