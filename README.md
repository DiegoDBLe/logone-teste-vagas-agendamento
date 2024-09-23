# Teste Log.One

Este projeto foi desenvolvido como parte do processo seletivo para a vaga de desenvolvedor Java na Log.One. O repositório completo pode ser acessado [aqui](https://github.com/DiegoDBLe/logone-teste-vagas-agendamento).

## Introdução

Para realizar a integração entre JSF e Spring Boot, escolhi a biblioteca JoinFaces, o que tornou o processo de configuração muito mais simples. Com ela, foi possível eliminar a necessidade de manipular arquivos como web.xml e faces-config.xml, reduzindo a complexidade e facilitando a implementação do JSF no ambiente Spring Boot. Essa escolha também proporcionou maior fluidez no desenvolvimento e garantiu uma integração mais eficiente entre as duas tecnologias. Além disso, para manter a qualidade do código e evitar potenciais bugs, organizei o projeto de uma maneira que favorece a legibilidade e a manutenção.
Em vez de seguir uma estrutura alternativa, segui o padrão de Arquitetura em Camadas, organizando o projeto nas camadas Controller, Service, Repository e Model. Essa abordagem ajuda a separar as responsabilidades, promovendo uma manutenção mais fácil e uma melhor legibilidade do código. Utilizei o modificador de acesso default para as camadas de repositórios e modelos, o que permite um desacoplamento eficiente e uma encapsulação adequada das classes.

## Funcionalidades

- **Cadastro de Solicitantes**: Permite adicionar novos solicitantes ao sistema.
- **Cadastro de Vagas**: Facilita o registro de vagas disponíveis dentro de um período específico.
- **Cadastro de Agendamentos**: Possibilita o registro de agendamentos em uma data definida.
- **Consulta de Agendamentos**: Oferece a funcionalidade de consultar agendamentos realizados em um determinado intervalo de datas.

# Clone o repositório do GitHub
git clone https://github.com/DiegoDBLe/logone-teste-vagas-agendamento

# Execute o projeto Spring Boot com Maven
- mvn spring-boot:run

## Tecnologias Utilizadas

- Java
- Spring Boot
- JSF
- HyperSQL

- localhost:9292 (no pdf do desafio de teste estava informando a porta 9494, mas a aplicação estava configurada na porta 9292).

## Imagens
- Consulta de Agendamentos
<img width="952" alt="image" src="https://github.com/user-attachments/assets/eb73ebed-38d5-4dd5-a5dc-31b1daba03de">

- Cadastro de Vagas
<img width="950" alt="image" src="https://github.com/user-attachments/assets/b8a459a9-51bc-4f23-b096-16c1e71ca83a">

- Cadastro de Solicitantes
<img width="950" alt="image" src="https://github.com/user-attachments/assets/a9bf9952-e621-431b-813f-8902c1640915">

- Cadastro de Agendamentos
<img width="947" alt="image" src="https://github.com/user-attachments/assets/61325baa-483d-491c-91f4-ed7511acae4e">

- Consulta de Agendamentos por periodo e retornando no grid as informações 
<img width="954" alt="image" src="https://github.com/user-attachments/assets/a9f9b9c3-116c-4ee0-8cf7-0b56035c7a41">

- Consulta de Agendamentos por periodo e por solicitante especifico no menuIteme e retornando no grid as informações
<img <img width="945" alt="image" src="https://github.com/user-attachments/assets/31068bf1-8cc9-4807-b080-b0b56a31e9b8">



