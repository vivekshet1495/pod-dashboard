window.addEventListener('load', () => {
  document.querySelectorAll('canvas[id$="_chart"]').forEach(canvas => {
    const data = {
      labels: ['cpu','mem'],
      datasets: [{
        label: 'Usage',
        data: [Math.random() * 100, Math.random() * 512],
        tension: 0.4
      }]
    };
    new Chart(canvas.getContext('2d'), { type: 'bar', data });
  });
});

