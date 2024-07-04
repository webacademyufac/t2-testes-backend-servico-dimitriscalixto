package br.ufac.sgcmapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ufac.sgcmapi.model.Atendimento;
import br.ufac.sgcmapi.model.EStatus;
import br.ufac.sgcmapi.model.Paciente;
import br.ufac.sgcmapi.model.Profissional;
import br.ufac.sgcmapi.repository.AtendimentoRepository;

@ExtendWith(MockitoExtension.class)
public class AtendimentoServiceTest {

  @Mock
  private AtendimentoRepository repo;
  @InjectMocks
  private AtendimentoService service;
  Atendimento a1;
  Atendimento a2;
  Paciente p1;
  List<Atendimento> atendimentos;

  @BeforeEach
  public void setUp() {
    atendimentos = new ArrayList<>();
    p1 = new Paciente();
    p1.setNome("Dimitris");
    p1.setId(1L);
    a1 = new Atendimento();
    a2 = new Atendimento();
    a1.setHora(LocalTime.of(14, 00));
    a2.setHora(LocalTime.of(15, 00));
    a1.setId(1L);
    a2.setId(2L);
    a1.setPaciente(p1);
    a2.setPaciente(p1);
    atendimentos.add(a1);
    atendimentos.add(a2);
  }

  @Test
  public void testeAtendimentoGetAll() {
    Mockito.when(repo.findAll()).thenReturn(atendimentos);
    List<Atendimento> result = service.get();
    assertEquals(2, result.size());
  }

  @Test
  public void testeAtendimentoGetById() {
    Mockito.when(repo.findById(1L)).thenReturn(Optional.of(a1));
    Atendimento result = service.get(1L);
    assertEquals(1, result.getId());
  }

  @Test
  void testAtendimentoTermoBusca() {
    Mockito.when(repo.busca("Dimitris")).thenReturn(atendimentos);
    List<Atendimento> buscaResult = service.get("Dimitris");
    Atendimento result = buscaResult.get(0);
    assertEquals("Dimitris", result.getPaciente().getNome());
  }

  @Test
  public void testAtendimentoDelete() {
    Mockito.doNothing().when(repo).deleteById(1L);
    repo.deleteById(1L);
    Mockito.verify(repo, times(1)).deleteById(1L);
  }

  @Test
  void testGetHorarios() {
    Mockito.when(repo.findByProfissionalAndDataAndStatusNot(Mockito.any(Profissional.class),
        Mockito.eq(LocalDate.now()), Mockito.eq(EStatus.CANCELADO))).thenReturn(atendimentos);

    List<String> results = service.getHorarios(1L, LocalDate.now());
    assertEquals(2, results.size());
  }

  @Test
  void testSave() {
    Mockito.when(repo.save(a1)).thenReturn(a1);
    assertEquals(a1, repo.save(a1));
  }

  @Test
  void testUpdateStatus() {
    Mockito.when(repo.findById(1L)).thenReturn(Optional.of(a1));
    Atendimento result = service.updateStatus(1L);
    assertEquals(EStatus.CONFIRMADO, result.getStatus());
  }
}
